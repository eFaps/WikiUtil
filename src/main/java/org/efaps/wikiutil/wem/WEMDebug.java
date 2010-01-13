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

package org.efaps.wikiutil.wem;

import java.io.PrintStream;
import java.net.URL;

/**
 * Wiki Event Manager which could be used for debugging purposes. Two modes
 * exists. If {@link  WEMDebug#wem} is defined (and not null) the original Wiki
 * Event Model is called. Otherwise only the information is printed to
 * {@link WEMDebug#out}.
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class WEMDebug
    implements IWikiEventModel
{
    /**
     * Print output stream used for debugging purposes.
     */
    private final PrintStream out;

    /**
     * Wiki event model which is called.
     */
    private final IWikiEventModel wem;

    /**
     * Indent of one printed line (increased for start method; decreased for
     * end methods).
     *
     * @see #println(String)
     */
    private int indent = 0;

    /**
     * Wiki event debugger without underlying Wiki event manager.
     *
     * @param _out      print output stream
     */
    public WEMDebug(final PrintStream _out)
    {
        this(_out, null);
    }

    /**
     * Wiki event debugger with underlying Wiki event manager
     * <code>_wem</code>.
     *
     * @param _out      print output stream
     * @param _wem      used Wiki event model
     */
    public WEMDebug(final PrintStream _out,
                    final IWikiEventModel _wem)
    {
        this.out = _out;
        this.wem = _wem;
    }

    /**
     * Prints given <code>_text</code> to the output stream {@link #out}.
     *
     * @param _text     text to print
     */
    protected void println(final String _text)
    {
        this.out.format("%1$02d  ", this.indent);
        for (int idx = 0; idx < this.indent; idx++)  {
            this.out.print("  ");
        }
        this.out.println(_text.replaceAll("\\n", "\\\\n"));
    }

    /**
     * {@inheritDoc}
     */
    public void onProperty(final EProperty _property,
                           final String _value)
    {
        println("onProperty(" + _property + ", value = " + _value + ")");
        if (this.wem != null)  {
            this.wem.onProperty(_property, _value);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void documentStart()
    {
        println("documentStart");
        this.indent++;
        if (this.wem != null)  {
            this.wem.documentStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void documentEnd()
    {
        this.indent--;
        println("documentEnd");
        if (this.wem != null)  {
            this.wem.documentEnd();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void sectionStart()
    {
        println("sectionStart");
        this.indent++;
        if (this.wem != null)  {
            this.wem.sectionStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void sectionEnd()
    {
        this.indent--;
        println("sectionEnd");
        if (this.wem != null)  {
            this.wem.sectionEnd();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void headingStart()
    {
        println("headingStart");
        this.indent++;
        if (this.wem != null)  {
            this.wem.headingStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void headingEnd()
    {
        this.indent--;
        println("headingEnd");
        if (this.wem != null)  {
            this.wem.headingEnd();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void paragraphStart()
    {
        println("paragraphStart");
        this.indent++;
        if (this.wem != null)  {
            this.wem.paragraphStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void paragraphEnd()
    {
        this.indent--;
        println("paragraphEnd");
        if (this.wem != null)  {
            this.wem.paragraphEnd();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void tableStart()
    {
        println("tableStart");
        this.indent++;
        if (this.wem != null)  {
            this.wem.tableStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void tableEnd()
    {
        this.indent--;
        println("tableEnd");
        if (this.wem != null)  {
            this.wem.tableEnd();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void tableBodyStart()
    {
        println("tableBodyStart");
        this.indent++;
        if (this.wem != null)  {
            this.wem.tableBodyStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void tableBodyEnd()
    {
        this.indent--;
        println("tableBodyEnd");
        if (this.wem != null)  {
            this.wem.tableBodyEnd();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void tableRowStart()
    {
        println("tableRowStart");
        this.indent++;
        if (this.wem != null)  {
            this.wem.tableRowStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void tableRowEnd()
    {
        this.indent--;
        println("tableRowEnd");
        if (this.wem != null)  {
            this.wem.tableRowEnd();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void tableEntryStart()
    {
        println("tableEntryStart");
        this.indent++;
        if (this.wem != null)  {
            this.wem.tableEntryStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void tableEntryEnd()
    {
        this.indent--;
        println("tableEntryEnd");
        if (this.wem != null)  {
            this.wem.tableEntryEnd();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void typefaceStart(final ETypeface _typeface)
    {
        println("typefaceStart(" + _typeface + ")");
        this.indent++;
        if (this.wem != null)  {
            this.wem.typefaceStart(_typeface);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void typefaceEnd(final ETypeface _typeface)
    {
        this.indent--;
        println("typefaceEnd");
        if (this.wem != null)  {
            this.wem.typefaceEnd(_typeface);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void listBulletedStart()
    {
        println("listBulletedStart");
        this.indent++;
        if (this.wem != null)  {
            this.wem.listBulletedStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void listBulletedEnd()
    {
        this.indent--;
        println("listBulletedEnd");
        if (this.wem != null)  {
            this.wem.listBulletedEnd();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void listNumberedStart()
    {
        println("listNumberedStart");
        this.indent++;
        if (this.wem != null)  {
            this.wem.listNumberedStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void listNumberedEnd()
    {
        this.indent--;
        println("listNumberedEnd");
        if (this.wem != null)  {
            this.wem.listNumberedEnd();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void listEntryStart()
    {
        println("listEntryStart");
        this.indent++;
        if (this.wem != null)  {
            this.wem.listEntryStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void listEntryEnd()
    {
        this.indent--;
        println("listEntryEnd");
        if (this.wem != null)  {
            this.wem.listEntryEnd();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void onDivider()
    {
        println("onDivider");
        if (this.wem != null)  {
            this.wem.onDivider();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void onImage(final URL _url)
    {
        println("onImage(" + _url + ")");
        if (this.wem != null)  {
            this.wem.onImage(_url);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void onPreformat(final CharSequence _text)
    {
        println("onPreformat(" + _text + ")");
        if (this.wem != null)  {
            this.wem.onPreformat(_text);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void onText(final CharSequence _text)
    {
        println("onText(" + _text + ")");
        if (this.wem != null)  {
            this.wem.onText(_text);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void onTableOfContents(final int _deepth)
    {
        println("onTableOfContents(" + _deepth + ")");
        if (this.wem != null)  {
            this.wem.onTableOfContents(_deepth);
        }
    }


    /**
     * {@inheritDoc}
     */
    public void onLinkExternal(final URL _url,
                               final CharSequence _description)
    {
        println("onLinkExternal(" + _url + ", description=" + _description + ")");
        if (this.wem != null)  {
            this.wem.onLinkExternal(_url, _description);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void onLinkInternal(final CharSequence _link,
                               final CharSequence _description)
    {
        println("onLinkInternal(" + _link + ", description=" + _description + ")");
        if (this.wem != null)  {
            this.wem.onLinkInternal(_link, _description);
        }
    }
}
