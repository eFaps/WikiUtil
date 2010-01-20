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
     * Level of the heading.
     */
    private int headingLevel = 0;

    /**
     * Depth of the table of Content.
     */
    private int toCDepth;

    /**
     * Should only a snipplet be created;
     */
    private boolean snipplet = true;



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
        if (!this.snipplet) {
            this.bldrs.peek().append("<html><body>");
        }
        if (this.wem != null)  {
            this.wem.documentStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void documentEnd()
    {
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
                        .append(entry.getValue()).append("</a></li>");
                    level = entry.getLevel();
                }
            }
            while (level > 0) {
                this.toCBldr.append("</ul>");
                level--;
            }
        }
        if (!this.snipplet) {
            this.bldrs.peek().append("</body></html>");
        }
        if (this.wem != null)  {
            this.wem.documentEnd();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void sectionStart()
    {
        this.headingLevel++;
        if (this.wem != null)  {
            this.wem.sectionStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void sectionEnd()
    {
        this.headingLevel--;
        if (this.wem != null)  {
            this.wem.sectionEnd();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void headingStart()
    {
        this.toC.push(new ToCEntry(this.headingLevel));
        this.heading = true;
        this.bldrs.peek().append("<h").append(this.headingLevel).append("><a name=\"");
        if (this.wem != null)  {
            this.wem.headingStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void headingEnd()
    {
        this.heading = false;
        this.bldrs.peek().append("</a></h").append(this.headingLevel).append(">");
        if (this.wem != null)  {
            this.wem.headingEnd();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void paragraphStart()
    {
        this.bldrs.peek().append("<p>");
        if (this.wem != null)  {
            this.wem.paragraphStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void paragraphEnd()
    {
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
        if (this.wem != null)  {
            this.wem.tableStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void tableEnd()
    {
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
        if (this.wem != null)  {
            this.wem.tableBodyStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void tableBodyEnd()
    {
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
        if (this.wem != null)  {
            this.wem.tableRowStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void tableRowEnd()
    {
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
        if (this.wem != null)  {
            this.wem.tableEntryStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void tableEntryEnd()
    {
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
        if (this.wem != null)  {
            this.wem.typefaceStart(_typeface);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void typefaceEnd(final ETypeface _typeface)
    {
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
        if (this.wem != null)  {
            this.wem.listBulletedStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void listBulletedEnd()
    {
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
        if (this.wem != null)  {
            this.wem.listNumberedStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void listNumberedEnd()
    {
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
        if (this.wem != null)  {
            this.wem.listEntryStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void listEntryEnd()
    {
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
        this.bldrs.peek().append("<a href=\"").append(_url).append("\">")
            .append(_description != null && _description.length() > 0
                            ? StringEscapeUtils.escapeHtml(_description.toString())
                            : _url).append("</a>");
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
        this.bldrs.peek().append("<a href=\"").append(_link).append("\">")
            .append(_description != null && _description.length() > 0
                            ? StringEscapeUtils.escapeHtml(_description.toString())
                            : _link).append("</a>");
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
     * Getter method for the instance variable {@link #snipplet}.
     *
     * @return value of instance variable {@link #snipplet}
     */
    public boolean isSnipplet()
    {
        return snipplet;
    }

    /**
     * Setter method for instance variable {@link #snipplet}.
     *
     * @param _snipplet value for instance variable {@link #snipplet}
     */

    public WEMHtml setSnipplet(final boolean _snipplet)
    {
        this.snipplet = _snipplet;
        return this;
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
         * Level of the header.
         */
        private final int headerLevel;

        /**
         * Value for this entry.
         */
        private CharSequence value;

        /**
         * @param _headerLevel level for the header
         */
        public ToCEntry(final int _headerLevel)
        {
            this.headerLevel = _headerLevel;
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
         * Get the level of the eheading as primitiv int.
         * @return level of the heading
         */
        public int getLevel()
        {
            return this.headerLevel;
        }
    }
}
