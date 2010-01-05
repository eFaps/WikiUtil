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

package org.efaps.wikiutil.wom.element.text;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.efaps.wikiutil.wom.element.AbstractLineElement;

/**
 * An internal link of a text element.
 *
 * @author The eFaps Team
 * @version $Id$
 */
public abstract class AbstractInternalLink
    extends AbstractLineElement
{

    /**
     * Defines the link string of the internal link.
     *
     * @see #InternalLink(String)
     * @see #getLink()
     */
    private final String link;

    /**
     * Initializes the {@link #link}.
     *
     * @param _link     link
     */
    public AbstractInternalLink(final CharSequence _link)
    {
        this.link = _link.toString();
    }

    /**
     * Returns the internal {@link #link}.
     *
     * @return link
     * @see #link
     */
    public String getLink()
    {
        return this.link;
    }


    /**
     * Returns the string representation of this class including the related
     * {@link #link}.
     *
     * @return string representation of this class
     */
    @Override()
    public String toString()
    {
        return new ToStringBuilder(this)
            .appendSuper(super.toString())
            .append("link", this.link)
            .toString();
    }
}
