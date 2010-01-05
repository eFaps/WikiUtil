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

package org.efaps.wikiutil.page.element;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Abstract definition of a section with sub sections (without header!).
 *
 * @param <SECTION>
 * @author The eFaps Team
 * @version $Id$
 */
public class AbstractSection<SECTION extends AbstractSection<?>>
    extends AbstractParagraphList<SECTION>
{
    /**
     * All sub sections for this section.
     *
     * @see #addSubSection(Section)
     * @see #getSubSections()
     */
    private final List<Section> subSections = new ArrayList<Section>();

    /**
     * Adds new <code>_subSection</code> to the list of {@link #subSections}.
     *
     * @param _subSection       sub section to append
     * @return this section instance
     */
    @SuppressWarnings("unchecked")
    public SECTION addSubSection(final Section _subSection)
    {
        this.subSections.add(_subSection);
        return (SECTION) this;
    }

    /**
     * Returns all assigned {@link #subSections} of a Wiki page.
     *
     * @return all sub sections
     * @see #subSections
     */
    public List<Section> getSubSections()
    {
        return this.subSections;
    }

    /**
     * Returns the string representation of this text string including the
     * {@link #subSections}.
     *
     * @return string representation
     */
    @Override()
    public String toString()
    {
        return new ToStringBuilder(this)
            .appendSuper(super.toString())
            .append("subSections", this.subSections)
            .toString();
    }
}
