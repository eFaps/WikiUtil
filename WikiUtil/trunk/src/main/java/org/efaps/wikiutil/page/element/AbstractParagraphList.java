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
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Abstract definition of line elements with text for Wiki pages.
 *
 * @param <ELEM>    element class derived from this class
 * @author The eFaps Team
 * @version $Id$
 */
public abstract class AbstractParagraphList<ELEM extends AbstractParagraphList<?>>
{
    /**
     * Holds the list of all elements.
     *
     * @see #getHeadings()
     */
    private final List<Paragraph> paragraphs = new ArrayList<Paragraph>();

    /**
     * Appends <code>_element</code> to the list of {@link #elements}.
     *
     * @param _element      element of a text to append
     * @return this text element of a Wiki page
     * @see #elements
     */
    @SuppressWarnings("unchecked")
    public ELEM add(final Paragraph _element)
    {
        this.paragraphs.add(_element);
        return (ELEM) this;
    }

    /**
     * Returns the last entry of {@link #paragraphs}.
     *
     * @return last paragraph
     * @see #paragraphs
     */
    public Paragraph lastParagraph()
    {
        return this.paragraphs.get(this.paragraphs.size() - 1);
    }

    /**
     * Appends <code>_elements</code> to the list of {@link #elements}.
     *
     * @param _elements     elements of a text to append
     * @return this text element of a Wiki page
     * @see #paragraphs
     */
    @SuppressWarnings("unchecked")
    public ELEM addAll(final Collection<Paragraph> _elements)
    {
        this.paragraphs.addAll(_elements);
        return (ELEM) this;
    }

    /**
     * Returns the list of embedded text elements.
     *
     * @return list of all text elements
     * @see #paragraphs
     */
    public List<Paragraph> getParagraphs()
    {
        return this.paragraphs;
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
            .appendSuper(super.toString())
            .append("paragraphs", this.paragraphs)
            .toString();
    }
}
