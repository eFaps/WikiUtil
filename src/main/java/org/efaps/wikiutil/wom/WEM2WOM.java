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

package org.efaps.wikiutil.wom;

import java.net.URL;
import java.util.Stack;

import org.efaps.wikiutil.parser.gwiki.EHeader;
import org.efaps.wikiutil.wem.EProperty;
import org.efaps.wikiutil.wem.ETypeface;
import org.efaps.wikiutil.wem.IWikiEventModel;
import org.efaps.wikiutil.wom.element.AbstractLineElement;
import org.efaps.wikiutil.wom.element.Divider;
import org.efaps.wikiutil.wom.element.Paragraph;
import org.efaps.wikiutil.wom.element.Preformat;
import org.efaps.wikiutil.wom.element.Section;
import org.efaps.wikiutil.wom.element.TableOfContents;
import org.efaps.wikiutil.wom.element.list.AbstractListEntry;
import org.efaps.wikiutil.wom.element.list.ListBulleted;
import org.efaps.wikiutil.wom.element.list.ListEntry;
import org.efaps.wikiutil.wom.element.list.ListNumbered;
import org.efaps.wikiutil.wom.element.table.Table;
import org.efaps.wikiutil.wom.element.table.TableCell;
import org.efaps.wikiutil.wom.element.table.TableRow;
import org.efaps.wikiutil.wom.element.text.AbstractTypeface;
import org.efaps.wikiutil.wom.element.text.ExternalLink;
import org.efaps.wikiutil.wom.element.text.ExternalLinkWithDescription;
import org.efaps.wikiutil.wom.element.text.Image;
import org.efaps.wikiutil.wom.element.text.InternalLink;
import org.efaps.wikiutil.wom.element.text.InternalLinkWithDescription;
import org.efaps.wikiutil.wom.element.text.TextString;
import org.efaps.wikiutil.wom.element.text.TypefaceBold;
import org.efaps.wikiutil.wom.element.text.TypefaceCode;
import org.efaps.wikiutil.wom.element.text.TypefaceItalic;
import org.efaps.wikiutil.wom.property.Label;
import org.efaps.wikiutil.wom.property.Sidebar;
import org.efaps.wikiutil.wom.property.Summary;

/**
 * Converts a Wiki stream from the Wiki event model to the Wiki object model.
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class WEM2WOM
    implements IWikiEventModel
{
    /**
     * A header is currently parsed.
     */
    private boolean inHeader;

    /**
     * Generated Wiki page.
     */
    private final WikiPage page = new WikiPage();

    /**
     * Current read section lect.
     *
     * @see #sectionStart(ESection)
     * @see #sectionEnd()
     */
    private final Stack<Section> sections = new Stack<Section>();

    /**
     * Current parsed table. Is <code>null</code> if currently no table is
     * parsed.
     *
     * @see #tableStart()
     * @see #tableEnd()
     */
    private Table table;

    /**
     * Current read table row. Is <code>null</code> if currently no table is
     * parsed.
     *
     * @see #tableRowStart()
     * @see #tableRowEnd()
     */
    private TableRow tableRow;

    /**
     * Current parsed table row. Is <code>null</code> if currently no table is
     * parsed.
     *
     * @see #tableEntryStart()
     * @see #tableEntryEnd()
     */
    private TableCell tableEntry;

    /**
     * Current read paragraph within current {@link #tableEntry}.
     *
     * @see #tableEntryStart()
     * @see #tableEntryEnd()
     */
    private Paragraph tableEntryParagraph;

    /**
     * @see #typefaceStart(ETypeface)
     * @see #typefaceEnd()
     */
    private final Stack<AbstractTypeface<?>> typeFaces = new Stack<AbstractTypeface<?>>();

    /**
     * Current read list element.
     */
    private final Stack<AbstractListEntry<?>> listDefis = new Stack<AbstractListEntry<?>>();

    private Paragraph paragraph;

    /**
     * Returns generated Wiki page.
     *
     * @return generated Wiki page
     * @see #page
     */
    public WikiPage getPage()
    {
        return this.page;
    }

    @Override()
    public void onProperty(final EProperty _property,
                           final String _value)
    {
        switch (_property)  {
            case DESCRIPTION:
                this.page.add(new Summary(_value));
                break;
            case KEY:
                this.page.add(new Label(_value));
                break;
            case SIDEBAR:
                this.page.add(new Sidebar(_value));
                break;
            default:
                System.err.println("unknown property " + _property + " " + _value);
        }
    }

    public void documentStart()
    {
    }

    public void documentEnd()
    {
    }

    public void sectionStart()
    {
        final Section section = new Section();
        if (this.sections.isEmpty())  {
            this.page.addSubSection(section);
        } else  {
            this.sections.peek().addSubSection(section);
        }
        this.sections.push(section);
    }

    public void sectionEnd()
    {
        this.sections.pop();
    }

    public void headingStart(final EHeader _eheader)
    {
        this.inHeader = true;
    }

    public void headingEnd(final EHeader _eheader)
    {
        this.inHeader = false;
    }

    public void paragraphStart()
    {
        if (!this.listDefis.isEmpty())  {
            final Paragraph para = new Paragraph();
            this.listDefis.peek().lastEntry().add(para);
        } else if (this.tableEntry != null)  {
            this.tableEntryParagraph = new Paragraph();
            this.tableEntry.add(this.tableEntryParagraph);
        } else  {
            this.paragraph = new Paragraph();
            if (this.sections.isEmpty())  {
                this.page.add(this.paragraph);
            } else  {
                this.sections.peek().add(this.paragraph);
            }
        }
    }

    public void paragraphEnd()
    {
    }

    /**
     * Table starts.
     */
    public void tableStart()
    {
        this.table = new Table();
        add(this.table);
    }

    /**
     * Table ends.
     */
    public void tableEnd()
    {
        this.table = null;
    }

    /**
     * Starts the table body (within a table).
     */
    public void tableBodyStart()
    {
    }

    /**
     * Ends the table body (within a table).
     */
    public void tableBodyEnd()
    {
    }

    /**
     * Starts a table row (within a table head, foot or body).
     */
    public void tableRowStart()
    {
        this.tableRow = new TableRow();
        this.table.addBodyRow(this.tableRow);
    }

    /**
     * Ends a table row (within a table head, foot or body).
     */
    public void tableRowEnd()
    {
        this.tableRow = null;
    }

    /**
     * Starts a table entry (within a table row).
     */
    public void tableEntryStart()
    {
        this.tableEntry = new TableCell();
        this.tableRow.add(this.tableEntry);
    }

    /**
     * Ends a table entry (within a table row).
     */
    public void tableEntryEnd()
    {
        this.tableEntry = null;
        this.tableEntryParagraph = null;
    }

    public void typefaceStart(final ETypeface _typeface)
    {
        switch (_typeface)  {
            case BOLD:
                final TypefaceBold bold = new TypefaceBold();
                add(bold);
                this.typeFaces.push(bold);
                break;
            case CODE:
                final TypefaceCode code = new TypefaceCode();
                add(code);
                this.typeFaces.push(code);
                break;
            case ITALIC:
                final TypefaceItalic italic = new TypefaceItalic();
                add(italic);
                this.typeFaces.push(italic);
                break;
            default:
                System.err.println("unknown type face " + _typeface);
        }
    }

    public void typefaceEnd(final ETypeface _typeface)
    {
        this.typeFaces.pop();
    }

    public void listBulletedStart()
    {
        final ListBulleted listDefi = new ListBulleted();
        add(listDefi);
        this.listDefis.add(listDefi);
    }

    public void listBulletedEnd()
    {
        this.listDefis.pop();
    }

    public void listNumberedStart()
    {
        final ListNumbered listDefi = new ListNumbered();
        add(listDefi);
        this.listDefis.add(listDefi);
    }

    public void listNumberedEnd()
    {
        this.listDefis.pop();
    }

    public void listEntryStart()
    {
        this.listDefis.peek().add(new ListEntry());
    }

    public void listEntryEnd()
    {
    }

    public void onDivider()
    {
        add(new Divider());
    }

    public void onImage(final URL _url)
    {
        add(new Image(_url));
    }

    public void onPreformat(final CharSequence _text)
    {
        this.paragraph.add(new Preformat(_text));
    }

    public void onText(final CharSequence _text)
    {
        add(new TextString(_text));
    }

    public void onTableOfContents(final int _deepth)
    {
        add(new TableOfContents(_deepth));
    }


    public void onLinkExternal(final URL _url,
                               final CharSequence _description)
    {
        if ((_description != null) && !"".equals(_description))  {
            add(new ExternalLinkWithDescription(_url, _description));
        } else  {
            add(new ExternalLink(_url));
        }
    }

    public void onLinkInternal(final CharSequence _link,
                               final CharSequence _description)
    {
        if ((_description != null) && !"".equals(_description))  {
            add(new InternalLinkWithDescription(_link, _description));
        } else  {
            add(new InternalLink(_link));
        }
    }

    protected void add(final AbstractLineElement _element)
    {
        if (!this.typeFaces.isEmpty()) {
            this.typeFaces.peek().add(_element);
        } else if (!this.listDefis.isEmpty())  {
            this.listDefis.peek().lastEntry().lastParagraph().add(_element);
        } else if (this.inHeader)  {
            this.sections.peek().addHeading(_element);
        } else if (this.tableEntryParagraph != null)  {
            this.tableEntryParagraph.add(_element);
        } else  {
            this.paragraph.add(_element);
        }
    }
}
