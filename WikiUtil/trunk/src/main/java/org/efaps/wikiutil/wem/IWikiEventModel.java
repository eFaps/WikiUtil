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

package org.efaps.wikiutil.wem;

import java.net.URL;


/**
 * Interface for the Wiki event model.
 *
 * @author The eFaps Team
 * @version $Id$
 */
public interface IWikiEventModel
{
    /**
     * Defines <code>_property</code> with <code>_value</code> of a Wiki page.
     *
     * @param _property     property type
     * @param _value        value of the property
     */
    void onProperty(final EProperty _property,
                    final String _value);

    /**
     * Start of document.
     */
    void documentStart();

    /**
     * End of document.
     */
    void documentEnd();

    /**
     * Start of section.
     */
    void sectionStart();

    /**
     * End of section.
     */
    void sectionEnd();

    /**
     * Start of heading (within a section).
     */
    void headingStart();

    /**
     * End of heading (within a section).
     */
    void headingEnd();

    /**
     * Start of paragraph (within a section or a document).
     */
    void paragraphStart();

    /**
     * End of paragraph (within a section or a document).
     */
    void paragraphEnd();

    /**
     * Table starts.
     */
    void tableStart();

    /**
     * Table ends.
     */
    void tableEnd();

    /**
     * Starts the table body (within a table).
     */
    void tableBodyStart();

    /**
     * Ends the table body (within a table).
     */
    void tableBodyEnd();

    /**
     * Starts a table row (within a table head, foot or body).
     */
    void tableRowStart();

    /**
     * Ends a table row (within a table head, foot or body).
     */
    void tableRowEnd();

    /**
     * Starts a table entry (within a table row).
     */
    void tableEntryStart();

    /**
     * Ends a table entry (within a table row).
     */
    void tableEntryEnd();

    /**
     * Start of a type face (within a paragraph or a heading).
     *
     * @param _typeface     type face
     */
    void typefaceStart(final ETypeface _typeface);

    /**
     * End of a type face (within a paragraph or a heading).
     *
     * @param _typeface     type face
     */
    void typefaceEnd(final ETypeface _typeface);

    /**
     * Start of a bulleted list.
     */
    void listBulletedStart();

    /**
     * End of a bulleted list.
     */
    void listBulletedEnd();

    /**
     * Start of a numbered list.
     */
    void listNumberedStart();

    /**
     * End of a numbered list.
     */
    void listNumberedEnd();

    /**
     * Start of a list entry.
     */
    void listEntryStart();

    /**
     * End of a list entry.
     */
    void listEntryEnd();

    /**
     * Line divider.
     */
    void onDivider();

    /**
     * New line.
     */
    void onNewLine();

    /**
     * Reads preformatted <code>_text</code>.
     *
     * @param _text     text
     */
    void onPreformat(final CharSequence _text);

    /**
     * Table of contents with defined <code>_deepth</code> is shown.
     *
     * @param _deepth   deepth of the table of contents
     */
    void onTableOfContents(final int _deepth);

    /**
     * Reads an image with <code>_url</code>.
     *
     * @param _url  URL of the image
     */
    void onImage(final URL _url);

    /**
     * ReadsA text is read.
     *
     * @param _text     current read text
     */
    void onText(final CharSequence _text);

    /**
     * Reads external link <code>_url</code> with <code>_description</code>.
     *
     * @param _url          URL of the link
     * @param _description  description of the link (could be
     *                      <code>null</code>)
     */
    void onLinkExternal(final URL _url,
                        final CharSequence _description);

    /**
     * Reads internal link <code>_link</code> with <code>_description</code>.
     *
     * @param _link         name of Wiki page as internal link (without
     *                      <code>.wiki</code> or other extensions)
     * @param _description  description of the link (could be
     *                      <code>null</code>)
     */
    void onLinkInternal(final CharSequence _link,
                        final CharSequence _description);
}
