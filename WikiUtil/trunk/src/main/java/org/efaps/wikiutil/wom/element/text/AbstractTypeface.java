/*
 * Copyright 2003 - 2011 The eFaps Team
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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.efaps.wikiutil.wom.element.AbstractLineElement;

/**
 * Abstract definition of type faces of the Wiki object model.
 *
 * @param <TYPEFACE>    type face class derived from this class
 * @author The eFaps Team
 * @version $Id$
 */
public abstract class AbstractTypeface<TYPEFACE extends AbstractTypeface<?>>
    extends AbstractLineElement
{
    /**
     * All elements of a Wiki page.
     */
    private final List<AbstractLineElement> elements = new ArrayList<AbstractLineElement>();

    /**
     * Returns all assigned {@link #elements} of a Wiki page.
     *
     * @return all elements
     * @see #elements
     */
    public List<AbstractLineElement> getElements()
    {
        return this.elements;
    }

    /**
     * Adds new <code>_element</code> to the list of {@link #elements}. The new
     * element is not a text element and so {@link #lastText} is set to
     * <code>null</code>.
     *
     * @param _element      element for one line to add
     * @return this wiki page instance
     */
    @SuppressWarnings("unchecked")
    public TYPEFACE add(final AbstractLineElement _element)
    {
        this.elements.add(_element);
        return (TYPEFACE) this;
    }

    /**
     * Returns the string representation of this text string including the
     * {@link #elements}.
     *
     * @return string representation
     */
    @Override
    public String toString()
    {
        return new ToStringBuilder(this)
            .appendSuper(super.toString())
            .append("elements", this.elements)
            .toString();
    }
}
