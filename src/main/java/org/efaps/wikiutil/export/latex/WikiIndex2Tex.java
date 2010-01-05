/*
 * Copyright 2003 - 2010 The eFaps Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Revision:        $Rev$
 * Last Changed:    $Date$
 * Last Changed By: $Author$
 */

package org.efaps.wikiutil.export.latex;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.efaps.wikiutil.parser.gwiki.GWikiParser;
import org.efaps.wikiutil.parser.gwiki.javacc.ParseException;
import org.efaps.wikiutil.wom.WikiPage;
import org.efaps.wikiutil.wom.element.AbstractLineElement;
import org.efaps.wikiutil.wom.element.Paragraph;
import org.efaps.wikiutil.wom.element.list.AbstractListEntry;
import org.efaps.wikiutil.wom.element.list.ListBulleted;
import org.efaps.wikiutil.wom.element.list.ListEntry;
import org.efaps.wikiutil.wom.element.list.ListNumbered;
import org.efaps.wikiutil.wom.element.text.AbstractInternalLink;
import org.efaps.wikiutil.wom.element.text.InternalLinkWithDescription;

/**
 * Uses an index page with bulleted or numbered lists (and sub lists) and
 * converts all existing referenced Wiki pages to one big latex book file.
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class WikiIndex2Tex
{
    /**
     * Name of the out file name (prefix, together with {@link #index}).
     *
     * @see #convert(int, ListEntry...)
     */
    private static final String OUT_FILENAME = "out";

    /**
     * Temporary directory where the output files are written.
     */
    private final File tempDir;

    /**
     * URL of the root directory where the wiki pages are located.
     */
    private final URL root;

    /**
     * File extensions of the wiki pages (e.g. <code>.wiki</code>).
     */
    private final String wikiExtension;

    /**
     * List element of the Wiki page which defines the index.
     *
     * @see #WikiIndex2Tex(URI)
     */
    private final AbstractListEntry<?> list;

    /**
     * Current index number of the {@link #OUT_FILENAME out} file.
     *
     * @see #convert(int, ListEntry...)
     */
    private int index = 0;

    /**
     * @param _tempDir          directory of the temporary directory where the
     *                          output files are written
     * @param _root             URI of the root path
     * @param _wikiExtension    extension of the Wiki files (including '.' if
     *                          required!)
     * @param _index            name of the index file
     * @throws IOException      if <code>_index</code> could not be opened
     * @throws ParseException   if index Wiki page could not be parsed
     */
    public WikiIndex2Tex(final File _tempDir,
                         final URI _root,
                         final String _wikiExtension,
                         final String _index)
        throws IOException, ParseException
    {
        this.tempDir = _tempDir;
        this.root = _root.toURL();
        this.wikiExtension = _wikiExtension;
        final WikiPage page = GWikiParser.parse(new URL(this.root, _index + this.wikiExtension).openStream(), "UTF8");
        AbstractListEntry<?> tmp = null;
        for (final Paragraph para : page.getParagraphs())  {
            for (final AbstractLineElement elem : para.getElements())  {
                if ((elem instanceof ListBulleted) || (elem instanceof ListNumbered))  {
                    tmp = (AbstractListEntry<?>) elem;
                    break;
                }
            }
        }
        this.list = tmp;
    }

    /**
     * Converts the Wiki pages to Tex files.
     *
     * @throws IOException if convert failed because files could not be opened
     *                     or written
     */
    public void convert()
        throws IOException
    {
        this.convert(0, this.list.getEntries().toArray(new ListEntry[this.list.getEntries().size()]));
        this.writeContentFile();
    }

    /**
     * Writes the content file which includes all generated single converted
     * wiki pages.
     *
     * @throws IOException if content file could not be written
     */
    protected void writeContentFile()
        throws IOException
    {
        final File content = new File(this.tempDir, "content.tex");
        Writer outp = null;
        try  {
            outp = new FileWriter(content);
            for (int idx = 0; idx < this.index; idx++)  {
                outp.append("\\input{")
                    .append(WikiIndex2Tex.OUT_FILENAME)
                    .append(String.valueOf(idx))
                    .append("}\n");
            }
        } finally  {
            if (outp != null)  {
                outp.close();
            }
        }
    }

    /**
     *
     * @param _level    level of the section
     * @param _entries  Wiki page entries to convert
     * @throws MalformedURLException if URL of the file is not correct
     */
    protected void convert(final int _level,
                           final ListEntry... _entries)
        throws MalformedURLException
    {
        for (final ListEntry entry : _entries)  {
            String link = null;
            String title = null;
            final List<ListEntry> subEntries = new ArrayList<ListEntry>();
            for (final Paragraph para : entry.getParagraphs())  {
                for (final AbstractLineElement elem : para.getElements())  {
                    if (elem instanceof AbstractInternalLink)  {
                        link = ((AbstractInternalLink) elem).getLink();
                        if (elem instanceof InternalLinkWithDescription)  {
                            title = ((InternalLinkWithDescription) elem).getDescription();
                        }
                    } else if ((elem instanceof ListBulleted) || (elem instanceof ListNumbered))  {
                        subEntries.addAll(((AbstractListEntry<?>) elem).getEntries());
                    }
                }
            }
            System.out.println("convert " + link);
// TODO: what to do if link is null?
            if (link != null)  {
                final URL url = new URL(this.root, link + this.wikiExtension);
                boolean exists = false;
                try {
                    final InputStream in = url.openStream();
                    in.close();
                    exists = true;
                } catch (final IOException e) {
                    e.printStackTrace();
                }
                if (exists)  {
                    final File file = new File(this.tempDir,
                                               WikiIndex2Tex.OUT_FILENAME + (this.index++) + ".tex");
                    try {
                        new WikiPage2Tex(url.toURI(), file, _level, title).convert();
                    } catch (final IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (final ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (final URISyntaxException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            if (!subEntries.isEmpty())  {
                this.convert(_level + 1, subEntries.toArray(new ListEntry[subEntries.size()]));
            }
        }
    }
}
