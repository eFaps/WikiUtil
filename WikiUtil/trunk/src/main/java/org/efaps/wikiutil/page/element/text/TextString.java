/*
 * Copyright 2003 - 2009 The eFaps Team
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

package org.efaps.wikiutil.page.element.text;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.efaps.wikiutil.page.element.AbstractLineElement;

/**
 * String of a text.
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class TextString
    extends AbstractLineElement
{
    /**
     * Related text of the string text element.
     */
    private final String text;

    /**
     * Initializes this text string instance with given <code>_chars</code> for
     * the {@link #text}.
     *
     * @param _chars    characters of the text string
     */
    public TextString(final CharSequence _chars)
    {
        this.text = _chars.toString();
    }

    /**
     * Returns the text which represents this string text element.
     *
     * @return text represent this string text element
     * @see #text
     */
    public String getText()
    {
        return this.text;
    }

    /**
     * Returns the string representation of this text string including the
     * {@link #text}.
     *
     * @return string representation
     */
    @Override()
    public String toString()
    {
        return new ToStringBuilder(this)
            .appendSuper(super.toString())
            .append("text", this.text)
            .toString();
    }
}
