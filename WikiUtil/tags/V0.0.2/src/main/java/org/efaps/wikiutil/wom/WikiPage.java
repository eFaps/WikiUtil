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

package org.efaps.wikiutil.wom;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.efaps.wikiutil.wom.element.AbstractSection;
import org.efaps.wikiutil.wom.property.AbstractProperty;

/**
 * Represents one Wiki page.
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class WikiPage
    extends AbstractSection<WikiPage>
{
    /**
     * All properties of this wiki page.
     *
     * @see #add(AbstractProperty)
     */
    private final List<AbstractProperty> properties = new ArrayList<AbstractProperty>();

    /**
     * Returns all {@link #properties} of this wiki page.
     *
     * @return properties of this wiki page
     * @see #properties
     */
    public List<AbstractProperty> getProperties()
    {
        return this.properties;
    }

    /**
     * Adds new <code>_element</code> to the list of {@link #elements}. The new
     * element is not a text element and so {@link #lastText} is set to
     * <code>null</code>.
     *
     * @param _element      header element to add
     * @return this Wiki page instance
     */
    public WikiPage add(final AbstractProperty _element)
    {
        this.properties.add(_element);
        return this;
    }

    /**
     * Returns the string representation of this text string including the
     * {@link #elements}.
     *
     * @return string representation
     */
    @Override()
    public String toString()
    {
        return new ToStringBuilder(this)
            .append("properties", this.properties)
            .appendSuper(super.toString())
            .toString();
    }
}
