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

package org.efaps.wikiutil.wom.element.list;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.efaps.wikiutil.wom.element.AbstractLineElement;

/**
 * Abstract definition of list entries.
 *
 * @param <ELEM>    list entry class derived from this class
 * @author The eFaps Team
 * @version $Id$
 */
public abstract class AbstractListEntry<ELEM extends AbstractListEntry<?>>
    extends AbstractLineElement
{
    /**
     * Child entries of a list entry.
     *
     * @see #getEntries()
     * @see #add(ListBulletedEntry)
     */
    private final List<ListEntry> entries = new ArrayList<ListEntry>();

    /**
     * Assigns list <code>_entry</code> to this list entry.
     *
     * @param _entry    list entry to assign
     * @return this instance
     * @see #entries
     */
    @SuppressWarnings("unchecked")
    public ELEM add(final ListEntry _entry)
    {
        this.entries.add(_entry);
        return (ELEM) this;
    }

    /**
     * Returns the last entry of {@link #entries}.
     *
     * @return last list entry
     * @see #entries
     */
    public ListEntry lastEntry()
    {
        return this.entries.get(this.entries.size() - 1);
    }

    /**
     * Returns all {@link #entries} of this list entry.
     *
     * @return all child entries
     * @see #entries
     */
    public List<ListEntry> getEntries()
    {
        return this.entries;
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
            .append("entries", this.entries)
            .toString();
    }
}
