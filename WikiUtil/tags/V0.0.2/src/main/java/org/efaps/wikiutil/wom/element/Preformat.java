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

package org.efaps.wikiutil.wom.element;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Class represents source code lines within a Wiki page.
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class Preformat
    extends AbstractLineElement
{
    /**
     * Source code.
     *
     * @see #Code(CharSequence)
     * @see #getCode()
     */
    private final String code;

    /**
     * Initializes this source code.
     *
     * @param _chars     source code lines
     */
    public Preformat(final CharSequence _chars)
    {
        this.code = _chars.toString();
    }

    /**
     * Returns the {@link #code} which represents this code text element.
     *
     * @return string represent this code text element
     * @see #code
     */
    public String getCode()
    {
        return this.code;
    }

    /**
     * Returns the string representation of this text string including the
     * {@link #code}.
     *
     * @return string representation
     */
    @Override()
    public String toString()
    {
        return new ToStringBuilder(this)
            .appendSuper(super.toString())
            .append("code", this.code)
            .toString();
    }
}
