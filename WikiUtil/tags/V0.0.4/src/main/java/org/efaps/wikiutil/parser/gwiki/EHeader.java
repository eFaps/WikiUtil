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

package org.efaps.wikiutil.parser.gwiki;

/**
 * Enumeration used to manage the read of headers.
 *
 * @author The eFaps Team
 * @version $Id$
 */
public enum EHeader
{
    /** Section without header (Wiki page itself). */
    PAGE()
    {
        /**
         * Returns always {@link EHeader#LEVEL1}.
         *
         * @return always {@link EHeader#LEVEL1}
         */
        @Override()
        public EHeader getSubLevel()
        {
            return EHeader.LEVEL1;
        }

        /**
         * Returns always <code>null</code> because no parent level exists.
         *
         * @return always <code>null</code>
         */
        @Override()
        public EHeader getParentLevel()
        {
            return null;
        }
    },
    /** Header level 1. */
    LEVEL1()
    {
        /**
         * Returns always {@link EHeader#LEVEL2}.
         *
         * @return always {@link EHeader#LEVEL2}
         */
        @Override()
        public EHeader getSubLevel()
        {
            return EHeader.LEVEL2;
        }

        /**
         * Returns always {@link EHeader#PAGE}.
         *
         * @return always {@link EHeader#PAGE}
         */
        @Override()
        public EHeader getParentLevel()
        {
            return EHeader.PAGE;
        }
    },
    /** Header level 2. */
    LEVEL2()
    {
        /**
         * Returns always {@link EHeader#LEVEL3}.
         *
         * @return always {@link EHeader#LEVEL3}
         */
        @Override()
        public EHeader getSubLevel()
        {
            return EHeader.LEVEL3;
        }

        /**
         * Returns always {@link EHeader#LEVEL1}.
         *
         * @return always {@link EHeader#LEVEL1}
         */
        @Override()
        public EHeader getParentLevel()
        {
            return EHeader.LEVEL1;
        }
    },
    /** Header level 3. */
    LEVEL3()
    {
        /**
         * Returns always {@link EHeader#LEVEL4}.
         *
         * @return always {@link EHeader#LEVEL4}
         */
        @Override()
        public EHeader getSubLevel()
        {
            return EHeader.LEVEL4;
        }

        /**
         * Returns always {@link EHeader#LEVEL2}.
         *
         * @return always {@link EHeader#LEVEL2}
         */
        @Override()
        public EHeader getParentLevel()
        {
            return EHeader.LEVEL2;
        }
    },
    /** Header level 4. */
    LEVEL4()
    {
        /**
         * Returns always {@link EHeader#LEVEL5}.
         *
         * @return always {@link EHeader#LEVEL5}
         */
        @Override()
        public EHeader getSubLevel()
        {
            return EHeader.LEVEL5;
        }

        /**
         * Returns always {@link EHeader#LEVEL3}.
         *
         * @return always {@link EHeader#LEVEL3}
         */
        @Override()
        public EHeader getParentLevel()
        {
            return EHeader.LEVEL3;
        }
    },
    /** Header level 5. */
    LEVEL5()
    {
        /**
         * Returns always {@link EHeader#LEVEL6}.
         *
         * @return always {@link EHeader#LEVEL6}
         */
        @Override()
        public EHeader getSubLevel()
        {
            return EHeader.LEVEL6;
        }

        /**
         * Returns always {@link EHeader#LEVEL4}.
         *
         * @return always {@link EHeader#LEVEL4}
         */
        @Override()
        public EHeader getParentLevel()
        {
            return EHeader.LEVEL4;
        }
    },
    /** Header level 6. */
    LEVEL6()
    {
        /**
         * Returns always <code>null</code> because no sub level exists.
         *
         * @return always <code>null</code>
         */
        @Override()
        public EHeader getSubLevel()
        {
            return null;
        }

        /**
         * Returns always {@link EHeader#LEVEL5}.
         *
         * @return always {@link EHeader#LEVEL5}
         */
        @Override()
        public EHeader getParentLevel()
        {
            return EHeader.LEVEL5;
        }
    };

    /**
     * Checks if given header level <code>_section</code> is sub level of this
     * level.
     *
     * @param _section  header level to check if child of this level
     * @return <i>true</i> if <code>_section</code> is child of this level;
     *         otherwise <i>false</i>
     */
    public boolean hasChild(final EHeader _section)
    {
        final EHeader subLevel = this.getSubLevel();
        final boolean ret;
        if (subLevel == null)  {
            ret = false;
        } else if (subLevel == _section)  {
            ret = true;
        } else  {
            ret = subLevel.hasChild(_section);
        }
        return ret;
    }

    /**
     * Returns the direct sub level.
     *
     * @return sub level
     */
    public abstract EHeader getSubLevel();

    /**
     * Returns the direct parent level.
     *
     * @return parent level
     */
    public abstract EHeader getParentLevel();
}
