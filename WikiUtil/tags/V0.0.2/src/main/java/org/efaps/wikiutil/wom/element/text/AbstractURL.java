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

import java.net.URL;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.efaps.wikiutil.wom.element.AbstractLineElement;

/**
 * An URL of a text element.
 *
 * @author The eFaps Team
 * @version $Id$
 */
abstract class AbstractURL
    extends AbstractLineElement
{
    /**
     * URL to the image.
     */
    private final URL url;

    /**
     * Initialize the image holder for the text.
     *
     * @param _url     URL
     */
    AbstractURL(final URL _url)
    {
        this.url = _url;
    }

    /**
     * Returns the {@link #url URL}.
     *
     * @return URL
     * @see #url
     */
    public URL getURL()
    {
        return this.url;
    }

    /**
     * Returns the string representation of this class including the related
     * {@link #url URL}.
     *
     * @return string representation of this class
     */
    @Override()
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.NO_FIELD_NAMES_STYLE)
            .appendSuper(super.toString())
            .append("url", this.url)
            .toString();
    }
}
