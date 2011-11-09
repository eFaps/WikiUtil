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

package org.efaps.wikiutil.wom.element.table;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.efaps.wikiutil.wom.element.AbstractLineElement;

/**
 * Table of a Wiki page.
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class Table
    extends AbstractLineElement
{
    /**
     * Body rows of a table.
     *
     * @see #getBodyRows()
     * @see #addBodyRow(TableRow)
     */
    private final List<TableRow> bodyRows = new ArrayList<TableRow>();

    /**
     * Returns all {@link #rows} of this table.
     *
     * @return all rows
     * @see #rows
     */
    public List<TableRow> getBodyRows()
    {
        return this.bodyRows;
    }

    /**
     * Appends given <code>_row</code> to the list of all {@link #bodyRows}.
     *
     * @param _row      row to append
     * @return this table instance
     * @see #bodyRows
     */
    public Table addBodyRow(final TableRow _row)
    {
        this.bodyRows.add(_row);
        return this;
    }

    /**
     * Returns the string representation of this text string including the
     * {@link #bodyRows}.
     *
     * @return string representation
     */
    @Override()
    public String toString()
    {
        return new ToStringBuilder(this)
            .appendSuper(super.toString())
            .append("bodyRows", this.bodyRows)
            .toString();
    }
}
