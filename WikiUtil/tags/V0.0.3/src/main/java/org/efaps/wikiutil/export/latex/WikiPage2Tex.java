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
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.efaps.wikiutil.parser.gwiki.GWikiParser;
import org.efaps.wikiutil.parser.gwiki.javacc.ParseException;
import org.efaps.wikiutil.wom.WikiPage;
import org.efaps.wikiutil.wom.element.AbstractLineElement;
import org.efaps.wikiutil.wom.element.AbstractParagraphList;
import org.efaps.wikiutil.wom.element.NewLine;
import org.efaps.wikiutil.wom.element.Paragraph;
import org.efaps.wikiutil.wom.element.Preformat;
import org.efaps.wikiutil.wom.element.Section;
import org.efaps.wikiutil.wom.element.list.ListBulleted;
import org.efaps.wikiutil.wom.element.list.ListEntry;
import org.efaps.wikiutil.wom.element.table.Table;
import org.efaps.wikiutil.wom.element.table.TableCell;
import org.efaps.wikiutil.wom.element.table.TableRow;
import org.efaps.wikiutil.wom.element.text.ExternalLink;
import org.efaps.wikiutil.wom.element.text.ExternalLinkWithDescription;
import org.efaps.wikiutil.wom.element.text.Image;
import org.efaps.wikiutil.wom.element.text.InternalLink;
import org.efaps.wikiutil.wom.element.text.InternalLinkWithDescription;
import org.efaps.wikiutil.wom.element.text.TextString;
import org.efaps.wikiutil.wom.element.text.TypefaceBold;
import org.efaps.wikiutil.wom.element.text.TypefaceCode;
import org.efaps.wikiutil.wom.element.text.TypefaceItalic;

/**
 * Converts one Wiki page to a Latex file.
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class WikiPage2Tex
{
    /**
     * Levels of the sections.
     */
    private static final String[] STRUCTURE = {
        "\\part",
        "\\chapter",
        "\\section",
        "\\subsection",
        "\\subsubsection",
        "\\paragraph",
        "\\subparagraph"
    };

    /**
     * Prefix used for tables (behind the {@link #texOut TeX file name}).
     *
     * @see #appendTable(Appendable, Table)
     */
    private static final String PREFIX_TABLE = "TABLE";

    /**
     * Prefix used for images (behind the {@link #texOut TeX file name}).
     *
     * @see #getImage(URL)
     */
    private static final String PREFIX_IMAGE = "IMAGE";

    /**
     * URL of the wiki file to convert.
     */
    private final URL url;

    /**
     * Output file. Uses also to generate the image file names.
     */
    private final File texOut;

    /**
     * Counter used to generate unique file names.
     *
     * @see #PREFIX_TABLE
     * @see #appendTable(Appendable, Table)
     */
    private int tableCount = 0;

    /**
     * @see #PREFIX_IMAGE
     * @see #getImage(URL)
     */
    private int imageCount = 0;

    /**
     * Current start section level.
     */
    private final int structureLevel;

    /**
     * Title of the wiki page.
     */
    private final String title;

    /**
     * @param _uri              URI of the input file
     * @param _out              output file
     * @param _structureLevel   current level of the section
     * @param _title            title of the Wiki page
     * @throws MalformedURLException if the <code>_uri</code> could not be
     *                               converted to an URL
     */
    public WikiPage2Tex(final URI _uri,
                        final File _out,
                        final int _structureLevel,
                        final String _title)
        throws MalformedURLException
    {
        this.url = _uri.toURL();
        this.texOut = _out;
        this.structureLevel = _structureLevel;
        this.title = _title;
    }

    /**
     *
     * @throws IOException              if <code>_out</code> or <code>_in</code>
     *                                  could not be opened or not written
     * @throws ParseException           if Wiki page defined with
     *                                  <code>_in</code> could not be parsed
     */
    public void convert()
        throws IOException, ParseException
    {
        this.texOut.getParentFile().mkdirs();
        final WikiPage page = GWikiParser.parse(this.url.openStream(), "UTF8");
        final Writer out = new FileWriter(this.texOut);
        try  {
            this.convert(out, page);
        } finally  {
            out.close();
        }
    }

    /**
     *
     * @param _out          appendable instance to the Latex file
     * @param _page         Wiki page to convert
     * @throws IOException if write failed
     */
    public void convert(final Appendable _out,
                        final WikiPage _page)
        throws IOException
    {
        // get title
        /*String title = null;
        for (final AbstractProperty prop : _page.getProperties())  {
            if (prop instanceof Summary)  {
                title = ((Summary) prop).getValue();
            }
        }*/
        _out.append(WikiPage2Tex.STRUCTURE[this.structureLevel]).append("{")
            .append(this.escape(this.title))
            .append("}\n");

        this.appendParagraph(_out, _page);
        for (final Section section : _page.getSubSections())  {
            this.appendSection(_out, section, 1);
        }
    }

    /**
     *
     * @param _out          appendable instance to the Latex file
     * @param _section      section to append
     * @param _level        level of the section header / title
     * @throws IOException if write failed
     */
    protected void appendSection(final Appendable _out,
                                 final Section _section,
                                 final int _level)
        throws IOException
    {
        // title
        for (final AbstractLineElement element : _section.getHeadings())  {
            _out.append(WikiPage2Tex.STRUCTURE[this.structureLevel + _level]).append("{");
            this.appendLineElement(_out, element);
            _out.append("}\n");
        }
        // text
        this.appendParagraph(_out, _section);
        // sub sections
        for (final Section section : _section.getSubSections())  {
            this.appendSection(_out, section, _level + 1);
        }
    }

    /**
     *
     * @param _out          appendable instance to the Latex file
     * @param _paragraphs   list of paragraphs to append
     * @throws IOException if write failed
     */
    protected void appendParagraph(final Appendable _out,
                                   final AbstractParagraphList<?> _paragraphs)
        throws IOException
    {
        for (final Paragraph paragraph : _paragraphs.getParagraphs())  {
            _out.append("\n\n");
            for (final AbstractLineElement element : paragraph.getElements())  {
                this.appendLineElement(_out, element);
            }
        }
    }

    /**
     *
     * @param _out      appendable instance to the Latex file
     * @param _element  line element of a paragraph to append
     * @throws IOException if write failed
     */
    protected void appendLineElement(final Appendable _out,
                                     final AbstractLineElement _element)
        throws IOException
    {
        if (_element instanceof ExternalLink)  {
            _out.append("\\url{")
                .append(((ExternalLink) _element).getURL().toExternalForm())
                .append("}");
        } else  if (_element instanceof ExternalLinkWithDescription)  {
            _out.append("\\href{")
                .append(((ExternalLinkWithDescription) _element).getURL().toExternalForm())
                .append("}{")
                .append(this.escape(((ExternalLinkWithDescription) _element).getDescription()))
                .append("}");
        } else if (_element instanceof ListBulleted)  {
            _out.append("\\begin{itemize}\n");
            for (final ListEntry entry : ((ListBulleted) _element).getEntries())  {
                _out.append("\\item {");
                this.appendParagraph(_out, entry);
                _out.append("}\n");
            }
            _out.append("\\end{itemize}\n");
        } else if (_element instanceof Image)  {
            _out.append(" \\includegraphics[width=\\textwidth]{")
                .append(this.getImage(((Image) _element).getURL()))
                .append("} ");
        } else if (_element instanceof InternalLink)  {
            _out.append(this.escape(((InternalLink) _element).getLink()));
        } else if (_element instanceof InternalLinkWithDescription)  {
            _out.append(this.escape(((InternalLinkWithDescription) _element).getDescription()));
        } else if (_element instanceof NewLine)  {
            _out.append(" \\newline ");
        } else if (_element instanceof Preformat)  {
            _out.append("\n\\begin{lstlisting}\n")
                .append(((Preformat) _element).getCode())
                .append("\n\\end{lstlisting}\n");
        } else if (_element instanceof Table)  {
            this.appendTable(_out, (Table) _element);
        } else if (_element instanceof TextString)  {
            _out.append(this.escape(((TextString) _element).getText()));
        } else if (_element instanceof TypefaceBold)  {
            _out.append("{\\bfseries ");
            for (final AbstractLineElement element : ((TypefaceBold) _element).getElements())  {
                this.appendLineElement(_out, element);
            }
            _out.append("}");
        } else if (_element instanceof TypefaceItalic)  {
            _out.append("{\\it ");
            for (final AbstractLineElement element : ((TypefaceItalic) _element).getElements())  {
                this.appendLineElement(_out, element);
            }
            _out.append("}");
        } else if (_element instanceof TypefaceCode)  {
            _out.append("{\\tt ");
            for (final AbstractLineElement element : ((TypefaceCode) _element).getElements())  {
                this.appendLineElement(_out, element);
            }
            _out.append("}");
        } else  {
            System.err.println("unknown class " + _element);
        }
    }

    /**
     * Appends to <code>_out</code> the Latex code for given
     * <code>_table</code>. As Latex a <code>longtable</code> is used which is
     * automatically sized and which could be on multiple pages.
     *
     * @param _out      appendable instance to the Latex file
     * @param _table    Wiki page table to write
     * @throws IOException if write failed
     */
    protected void appendTable(final Appendable _out,
                               final Table _table)
        throws IOException
    {
        // calculate table file name
        final String tableName = this.texOut.getName().replace('.', '_')
                                    + WikiPage2Tex.PREFIX_TABLE
                                    + (this.tableCount++);

        // calculate maximum entries in a row
        int max = 0;
        for (final TableRow row : _table.getBodyRows())  {
            if (max < row.getEntries().size())  {
                max = row.getEntries().size();
            }
        }
        _out.append("\n\\begin{filecontents*}{").append(tableName).append(".tex}")
            .append("\n\\begin{longtable}{|");
        for (int idx = 0; idx < max; idx++)  {
            _out.append("X|");
        }
        _out.append("}\n\\hline\n\\endhead\n\\hline\n\\endfoot\n");

        for (final TableRow row : _table.getBodyRows())  {
            boolean first = true;
            for (final TableCell cell : row.getEntries())  {
                if (first)  {
                    first = false;
                } else  {
                    _out.append(" & ");
                }
                this.appendParagraph(_out, cell);
            }
            _out.append(" \\\\\n\\hline\n");
        }
        _out.append("\\end{longtable}\n")
            .append("\\end{filecontents*}\n")
            .append("\\LTXtable{\\textwidth}{").append(tableName).append("}\n");
    }

    /**
     * Copies the image on <code>_url</code> to a local image with the name of
     * the {@link #texOut TeX file}, the {@link #PREFIX_IMAGE image prefix},
     * the unique {@link #imageCount image counter} and the original file
     * extension. The local used file name is returned.
     *
     * @param _url  URL of the image
     * @return name of image used internally
     * @throws IOException if image could not copied from <code>_url</code> to
     *                     a temporary local image file
     */
    protected String getImage(final URL _url)
        throws IOException
    {
        final String file = _url.getFile();
//        System.out.println("get image " + _url);

        final File imgFile = new File(this.texOut.getParent(),
                                      this.texOut.getName().replace('.', '_')
                                              + WikiPage2Tex.PREFIX_IMAGE
                                              + (this.imageCount++)
                                              + file.substring(file.lastIndexOf('.')));

        final OutputStream out = new FileOutputStream(imgFile);
        try  {
            IOUtils.copy(_url.openStream(), out);
        } finally  {
            out.close();
        }
        return imgFile.getName();
    }

    /**
     * Escapes all special characters from given <code>_text</code> string to
     * a Latex text string.
     *
     * @param _text text to escape
     * @return escaped text
     */
    protected String escape(final String _text)
    {
        return _text
                .replaceAll("\\\\", "\\\\textbackslash ")
                .replaceAll("_", "\\\\_")
                .replaceAll("%", "\\\\%")
                .replaceAll("&", "\\\\&")
                .replaceAll("\\^", "\\\\^{}")
                .replaceAll("~", "\\\\~{}")
                .replaceAll("#", "\\\\#")
                .replaceAll("\\u20AC", "\\\\euro") // euro sign
                .replaceAll("\\u2212", "-")        // unicode  minus
                .replaceAll("\\u2011", "-");        // unicode???
    }
}
