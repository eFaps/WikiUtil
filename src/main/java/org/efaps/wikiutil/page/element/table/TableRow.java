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

package org.efaps.wikiutil.page.element.table;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Represents one row of a table within a Wiki page.
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class TableRow
{
    /**
     * All entries of this table row.
     *
     * @see #getEntries()
     * @see #add(TableCell)
     */
    private final List<TableCell> entries = new ArrayList<TableCell>();

    /**
     * Returns all {@link #entries} of this table row.
     *
     * @return all entries
     * @see #entries
     */
    public List<TableCell> getEntries()
    {
        return this.entries;
    }

    /**
     * Appends given <code>_entry</code> to the list of all {@link #entries}.
     *
     * @param _entry    entry to append
     * @return this table row instance
     * @see #entries
     */
    public TableRow add(final TableCell _entry)
    {
        this.entries.add(_entry);
        return this;
    }

    /**
     * Returns the string representation of this text string including the
     * {@link #entries}.
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
