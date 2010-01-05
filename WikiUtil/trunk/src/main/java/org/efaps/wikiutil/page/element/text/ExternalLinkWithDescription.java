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

import java.net.URL;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * An external link with description of a text.
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class ExternalLinkWithDescription
    extends AbstractExternalLink
{
    /**
     * Description of the text link. If not defined the value is
     * <code>null</code>.
     */
    private final String description;

    /**
     * Initialize a link (with description) for the text.
     *
     * @param _url          URL
     * @param _description  description of the URL
     */
    public ExternalLinkWithDescription(final URL _url,
                                       final CharSequence _description)
    {
        super(_url);
        this.description = _description.toString();
    }

    /**
     * Returns the description of the URL.
     *
     * @return description of the URL
     * @see #description
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * Returns the string representation of this external link including the
     * {@link #description}.
     *
     * @return string representation
     */
    @Override()
    public String toString()
    {
        return new ToStringBuilder(this)
            .appendSuper(super.toString())
            .append("description", this.description)
            .toString();
    }
}
