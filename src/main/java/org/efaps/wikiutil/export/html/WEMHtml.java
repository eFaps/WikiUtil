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

package org.efaps.wikiutil.export.html;


import java.net.URL;
import java.util.Stack;

import org.apache.commons.lang.StringEscapeUtils;

import org.efaps.wikiutil.parser.gwiki.EHeader;
import org.efaps.wikiutil.wem.EProperty;
import org.efaps.wikiutil.wem.ETypeface;
import org.efaps.wikiutil.wem.IWikiEventModel;


/**
 * Wiki Event Manager which is used to create a valid html page.
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class WEMHtml
    implements IWikiEventModel
{
    /**
     * List of StringBuilders used to create the complete Html-String.
     */
    private final Stack<StringBuilder> bldrs = new Stack<StringBuilder>();

    /**
     * Stack for the entries of a table of contents.
     */
    private final Stack<ToCEntry> toC = new Stack<ToCEntry>();

    /**
     * Is currently a heading rendered.
     */
    private boolean heading;

    /**
     * StringBuilder used for the table of content.
     */
    private StringBuilder toCBldr;

    /**
     * Wiki event model which is called.
     */
    private final IWikiEventModel wem;

    /**
     * Indent of one printed line (increased for start method; decreased for
     * end methods).
     *
     * @see #println(String)
     */
    private int indent = 0;

    /**
     * Depth of the table of Content.
     */
    private int toCDepth;

    /**
     * Wiki event debugger without underlying Wiki event manager.
     *
     */
    public WEMHtml()
    {
        this(null);
    }

    /**
     * Wiki event debugger with underlying Wiki event manager
     * <code>_wem</code>.
     *
     * @param _wem      used Wiki event model
     */
    public WEMHtml(final IWikiEventModel _wem)
    {
        this.bldrs.push(new StringBuilder());
        this.wem = _wem;
    }

    /**
     * {@inheritDoc}
     */
    public void onProperty(final EProperty _property,
                           final String _value)
    {
        switch (_property) {
            case DESCRIPTION:
                this.bldrs.peek().append("<div class=\"eFapsWikiDescription\">").append(_value).append("</div>");
                break;
            default:
                break;
        }
        if (this.wem != null)  {
            this.wem.onProperty(_property, _value);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void documentStart()
    {
        this.bldrs.peek().append("<html><body>");
        this.indent++;
        if (this.wem != null)  {
            this.wem.documentStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void documentEnd()
    {
        this.indent--;
        if (this.toCBldr != null) {
            this.toCBldr.append("<ul>");
            int level = 1;
            for (final ToCEntry entry : this.toC) {
                if (this.toCDepth >= entry.getLevel()) {
                    if (entry.getLevel() > level) {
                        this.toCBldr.append("<ul>");
                    } else if (entry.getLevel() < level) {
                        this.toCBldr.append("</ul>");
                    }
                    this.toCBldr.append("<li><a href=\"#").append(entry.getHref()).append("\">")
                        .append(entry.getValue()).append("</li>");
                    level = entry.getLevel();
                }
            }
            while (level > 0) {
                this.toCBldr.append("</ul>");
                level--;
            }
        }

        this.bldrs.peek().append("</body></html>");
        if (this.wem != null)  {
            this.wem.documentEnd();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void sectionStart()
    {
        //TODO must there be done something?
        this.indent++;
        if (this.wem != null)  {
            this.wem.sectionStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void sectionEnd()
    {
        this.indent--;
        if (this.wem != null)  {
            this.wem.sectionEnd();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void headingStart(final EHeader _eheader)
    {
        this.toC.push(new ToCEntry(_eheader));
        this.heading = true;
        switch (_eheader) {
            case LEVEL1:
                this.bldrs.peek().append("<h1><a name=\"");
                break;
            case LEVEL2:
                this.bldrs.peek().append("<h2><a name=\"");
                break;
            case LEVEL3:
                this.bldrs.peek().append("<h3><a name=\"");
                break;
            case LEVEL4:
                this.bldrs.peek().append("<h4><a name=\"");
                break;
            case LEVEL5:
                this.bldrs.peek().append("<h5><a name=\"");
                break;
            case LEVEL6:
                this.bldrs.peek().append("<h6><a name=\"");
                break;
            default:
                break;
        }
        this.indent++;
        if (this.wem != null)  {
            this.wem.headingStart(_eheader);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void headingEnd(final EHeader _eheader)
    {
        this.heading = false;
        this.indent--;
        switch (_eheader) {
            case LEVEL1:
                this.bldrs.peek().append("</a></h1>");
                break;
            case LEVEL2:
                this.bldrs.peek().append("</a></h2>");
                break;
            case LEVEL3:
                this.bldrs.peek().append("</a></h3>");
                break;
            case LEVEL4:
                this.bldrs.peek().append("</a></h4>");
                break;
            case LEVEL5:
                this.bldrs.peek().append("</a></h5>");
                break;
            case LEVEL6:
                this.bldrs.peek().append("</a></h6>");
                break;
            default:
                break;
        }
        if (this.wem != null)  {
            this.wem.headingEnd(_eheader);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void paragraphStart()
    {
        this.bldrs.peek().append("<p>");
        this.indent++;
        if (this.wem != null)  {
            this.wem.paragraphStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void paragraphEnd()
    {
        this.indent--;
        this.bldrs.peek().append("</p>");
        if (this.wem != null)  {
            this.wem.paragraphEnd();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void tableStart()
    {
        this.bldrs.peek().append("<table>");
        this.indent++;
        if (this.wem != null)  {
            this.wem.tableStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void tableEnd()
    {
        this.indent--;
        this.bldrs.peek().append("</table>");
        if (this.wem != null)  {
            this.wem.tableEnd();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void tableBodyStart()
    {
        this.bldrs.peek().append("<tbody>");
        this.indent++;
        if (this.wem != null)  {
            this.wem.tableBodyStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void tableBodyEnd()
    {
        this.indent--;
        this.bldrs.peek().append("</tbody>");
        if (this.wem != null)  {
            this.wem.tableBodyEnd();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void tableRowStart()
    {
        this.bldrs.peek().append("<tr>");
        this.indent++;
        if (this.wem != null)  {
            this.wem.tableRowStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void tableRowEnd()
    {
        this.indent--;
        this.bldrs.peek().append("</tr>");
        if (this.wem != null)  {
            this.wem.tableRowEnd();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void tableEntryStart()
    {
        this.bldrs.peek().append("<td>");
        this.indent++;
        if (this.wem != null)  {
            this.wem.tableEntryStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void tableEntryEnd()
    {
        this.indent--;
        this.bldrs.peek().append("</td>");
        if (this.wem != null)  {
            this.wem.tableEntryEnd();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void typefaceStart(final ETypeface _typeface)
    {
        switch (_typeface) {
            case BOLD:
                this.bldrs.peek().append("<span style=\"font-weight:bold;\">");
                break;
            case CODE:
                this.bldrs.peek().append("<span style=\"font-family:monospace;\">");
                break;
            case ITALIC:
                this.bldrs.peek().append("<span style=\"font-style:italic;\">");
                break;
            case STRIKEOUT:
                this.bldrs.peek().append("<span style=\"text-decoration:line-through;\">");
                break;
            case SUB:
                this.bldrs.peek().append("<span style=\"vertical-align:sub;\">");
                break;
            case SUPER:
                this.bldrs.peek().append("<span style=\"vertical-align:super;\">");
                break;
            default:
                break;
        }
        this.indent++;
        if (this.wem != null)  {
            this.wem.typefaceStart(_typeface);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void typefaceEnd(final ETypeface _typeface)
    {
        this.indent--;
        this.bldrs.peek().append("</span>");
        if (this.wem != null)  {
            this.wem.typefaceEnd(_typeface);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void listBulletedStart()
    {
        this.bldrs.peek().append("<ul>");
        this.indent++;
        if (this.wem != null)  {
            this.wem.listBulletedStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void listBulletedEnd()
    {
        this.indent--;
        this.bldrs.peek().append("</ul>");
        if (this.wem != null)  {
            this.wem.listBulletedEnd();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void listNumberedStart()
    {
        this.bldrs.peek().append("<ol>");
        this.indent++;
        if (this.wem != null)  {
            this.wem.listNumberedStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void listNumberedEnd()
    {
        this.indent--;
        this.bldrs.peek().append("</ol>");
        if (this.wem != null)  {
            this.wem.listNumberedEnd();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void listEntryStart()
    {
        this.bldrs.peek().append("<li>");
        this.indent++;
        if (this.wem != null)  {
            this.wem.listEntryStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void listEntryEnd()
    {
        this.indent--;
        this.bldrs.peek().append("</li>");
        if (this.wem != null)  {
            this.wem.listEntryEnd();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void onDivider()
    {
        this.bldrs.peek().append("<hr/>");
        if (this.wem != null)  {
            this.wem.onDivider();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void onImage(final URL _url)
    {
        //TODO
        if (this.wem != null)  {
            this.wem.onImage(_url);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void onPreformat(final CharSequence _text)
    {
       //TODO
        if (this.wem != null)  {
            this.wem.onPreformat(_text);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void onText(final CharSequence _text)
    {
        final String html = StringEscapeUtils.escapeHtml(_text.toString());
        if (this.heading) {
            this.toC.peek().setValue(html);
            this.bldrs.peek().append(this.toC.peek().getHref()).append("\">");
        }
        this.bldrs.peek().append(html);
        if (this.wem != null)  {
            this.wem.onText(_text);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void onTableOfContents(final int _depth)
    {
        this.toCDepth = _depth;
        this.toCBldr = new StringBuilder();
        this.bldrs.push(this.toCBldr);
        this.bldrs.push(new StringBuilder());
        if (this.wem != null)  {
            this.wem.onTableOfContents(_depth);
        }
    }


    /**
     * {@inheritDoc}
     */
    public void onLinkExternal(final URL _url,
                               final CharSequence _description)
    {
        //TODO
        if (this.wem != null)  {
            this.wem.onLinkExternal(_url, _description);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void onLinkInternal(final CharSequence _link,
                               final CharSequence _description)
    {
        //TODO
        if (this.wem != null)  {
            this.wem.onLinkInternal(_link, _description);
        }
    }

    /**
     * @return whole html
     */
    public String getHtml()
    {
        final StringBuilder ret = new StringBuilder();
        for (final StringBuilder bldr : this.bldrs) {
            ret.append(bldr);
        }
        return ret.toString();
    }

    /**
     * @see java.lang.Object#toString()
     * @return value of function {@link #getHtml()}
     */
    @Override
    public String toString()
    {
        return getHtml();

    }

    /**
     * Entry for Table of Contents.
     */
    protected class ToCEntry
    {
        /**
         * EHeader for this entry.
         */
        private final EHeader eheader;

        /**
         * Value for this entry.
         */
        private CharSequence value;

        /**
         * @param _eheader EHeader
         */
        public ToCEntry(final EHeader _eheader)
        {
            this.eheader = _eheader;
        }

        /**
         * Get the values with invalid characters escaped.
         * @return the string used as the href
         */
        public String getHref()
        {
            return this.value.toString().replace(" ", "_");
        }

        /**
         * Getter method for instance variable {@link #value}.
         *
         * @return value of instance variable {@link #value}
         */
        public CharSequence getValue()
        {
            return this.value;
        }

        /**
         * Setter method for instance variable {@link #value}.
         *
         * @param _value value for instance variable {@link #value}
         */
        public void setValue(final CharSequence _value)
        {
            this.value = _value;
        }

        /**
         * Getter method for instance variable {@link #eheader}.
         *
         * @return value of instance variable {@link #eheader}
         */
        public EHeader getEheader()
        {
            return this.eheader;
        }

        /**
         * Get the level of the eheading as primitiv int.
         * @return level of the heading
         */
        public int getLevel()
        {
            final int ret;
            switch (this.eheader) {
                case LEVEL1:
                    ret = 1;
                    break;
                case LEVEL2:
                    ret = 2;
                    break;
                case LEVEL3:
                    ret = 3;
                    break;
                case LEVEL4:
                    ret = 4;
                    break;
                case LEVEL5:
                    ret = 5;
                    break;
                case LEVEL6:
                    ret = 6;
                    break;
                default:
                    ret = 0;
                    break;
            }
            return ret;
        }
    }
}
