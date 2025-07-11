/*
 * Copyright © 2003 - 2024 The eFaps Team (-)
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
 */
package org.efaps.wikiutil.wom.element;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Defines the text for a heading of level 1.
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class Section
    extends AbstractSection<Section>
{
    /**
     * All elements of a Wiki page.
     */
    private final List<AbstractLineElement> headings = new ArrayList<AbstractLineElement>();

    /**
     * Returns all assigned {@link #headings} of a Wiki page.
     *
     * @return all elements
     * @see #headings
     */
    public List<AbstractLineElement> getHeadings()
    {
        return this.headings;
    }

    /**
     * Adds new <code>_element</code> to the list of {@link #headings}. The new
     * element is not a text element and so {@link #lastText} is set to
     * <code>null</code>.
     *
     * @param _element      element for one line to add
     * @return this wiki page instance
     */
    public Section addHeading(final AbstractLineElement _element)
    {
        this.headings.add(_element);
        return this;
    }

    /**
     * Returns the string representation of this text string including the
     * {@link #headings}.
     *
     * @return string representation
     */
    @Override
    public String toString()
    {
        return new ToStringBuilder(this)
            .append("headings", this.headings)
            .appendSuper(super.toString())
            .toString();
    }
}
