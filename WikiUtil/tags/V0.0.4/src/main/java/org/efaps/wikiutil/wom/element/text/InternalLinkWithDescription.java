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

/**
 * An internal link with description within a text.
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class InternalLinkWithDescription
    extends AbstractInternalLink
{
    /**
     * Description of the internal link.
     */
    private final String description;

    /**
     * Initializes this internal link with {@link #description}.
     *
     * @param _link         internal link
     * @param _description  description of the internal link
     */
    public InternalLinkWithDescription(final CharSequence _link,
                                       final CharSequence _description)
    {
        super(_link);
        this.description = _description.toString();
    }

    /**
     * Returns the description of this internal link.
     *
     * @return description of the internal link
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
