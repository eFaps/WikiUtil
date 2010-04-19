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
 * Defines a table of contents within a Wiki page.
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class TableOfContents
    extends AbstractLineElement
{
    /**
     * Maximum depth of the table of contents.
     */
    private final int maxDepth;

    /**
     *
     * @param _maxDepth     maximum depth of the table of contents
     */
    public TableOfContents(final int _maxDepth)
    {
        this.maxDepth = _maxDepth;
    }

    /**
     * Returns the maximum depth of the table of contents.
     *
     * @return maximum depth
     */
    public int getMaxDepth()
    {
        return this.maxDepth;
    }

    /**
     * Returns the string representation of this text string including the
     * {@link #maxDepth}.
     *
     * @return string representation
     */
    @Override()
    public String toString()
    {
        return new ToStringBuilder(this)
            .appendSuper(super.toString())
            .append("maxDepth", this.maxDepth)
            .toString();
    }
}
