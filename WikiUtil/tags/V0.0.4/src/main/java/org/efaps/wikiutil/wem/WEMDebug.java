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
    @Override
    public void onProperty(final EProperty _property,
                           final String _value)
    {
        this.println("onProperty(" + _property + ", value = " + _value + ")");
        if (this.wem != null)  {
            this.wem.onProperty(_property, _value);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void documentStart()
    {
        this.println("documentStart");
        this.indent++;
        if (this.wem != null)  {
            this.wem.documentStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void documentEnd()
    {
        this.indent--;
        this.println("documentEnd");
        if (this.wem != null)  {
            this.wem.documentEnd();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sectionStart()
    {
        this.println("sectionStart");
        this.indent++;
        if (this.wem != null)  {
            this.wem.sectionStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sectionEnd()
    {
        this.indent--;
        this.println("sectionEnd");
        if (this.wem != null)  {
            this.wem.sectionEnd();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void headingStart()
    {
        this.println("headingStart");
        this.indent++;
        if (this.wem != null)  {
            this.wem.headingStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void headingEnd()
    {
        this.indent--;
        this.println("headingEnd");
        if (this.wem != null)  {
            this.wem.headingEnd();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paragraphStart()
    {
        this.println("paragraphStart");
        this.indent++;
        if (this.wem != null)  {
            this.wem.paragraphStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paragraphEnd()
    {
        this.indent--;
        this.println("paragraphEnd");
        if (this.wem != null)  {
            this.wem.paragraphEnd();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void tableStart()
    {
        this.println("tableStart");
        this.indent++;
        if (this.wem != null)  {
            this.wem.tableStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void tableEnd()
    {
        this.indent--;
        this.println("tableEnd");
        if (this.wem != null)  {
            this.wem.tableEnd();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void tableBodyStart()
    {
        this.println("tableBodyStart");
        this.indent++;
        if (this.wem != null)  {
            this.wem.tableBodyStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void tableBodyEnd()
    {
        this.indent--;
        this.println("tableBodyEnd");
        if (this.wem != null)  {
            this.wem.tableBodyEnd();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void tableRowStart()
    {
        this.println("tableRowStart");
        this.indent++;
        if (this.wem != null)  {
            this.wem.tableRowStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void tableRowEnd()
    {
        this.indent--;
        this.println("tableRowEnd");
        if (this.wem != null)  {
            this.wem.tableRowEnd();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void tableEntryStart()
    {
        this.println("tableEntryStart");
        this.indent++;
        if (this.wem != null)  {
            this.wem.tableEntryStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void tableEntryEnd()
    {
        this.indent--;
        this.println("tableEntryEnd");
        if (this.wem != null)  {
            this.wem.tableEntryEnd();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void typefaceStart(final ETypeface _typeface)
    {
        this.println("typefaceStart(" + _typeface + ")");
        this.indent++;
        if (this.wem != null)  {
            this.wem.typefaceStart(_typeface);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void typefaceEnd(final ETypeface _typeface)
    {
        this.indent--;
        this.println("typefaceEnd");
        if (this.wem != null)  {
            this.wem.typefaceEnd(_typeface);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void listBulletedStart()
    {
        this.println("listBulletedStart");
        this.indent++;
        if (this.wem != null)  {
            this.wem.listBulletedStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void listBulletedEnd()
    {
        this.indent--;
        this.println("listBulletedEnd");
        if (this.wem != null)  {
            this.wem.listBulletedEnd();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void listNumberedStart()
    {
        this.println("listNumberedStart");
        this.indent++;
        if (this.wem != null)  {
            this.wem.listNumberedStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void listNumberedEnd()
    {
        this.indent--;
        this.println("listNumberedEnd");
        if (this.wem != null)  {
            this.wem.listNumberedEnd();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void listEntryStart()
    {
        this.println("listEntryStart");
        this.indent++;
        if (this.wem != null)  {
            this.wem.listEntryStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void listEntryEnd()
    {
        this.indent--;
        this.println("listEntryEnd");
        if (this.wem != null)  {
            this.wem.listEntryEnd();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDivider()
    {
        this.println("onDivider");
        if (this.wem != null)  {
            this.wem.onDivider();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onNewLine()
    {
        this.println("onNewLine");
        if (this.wem != null)  {
            this.wem.onNewLine();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onImage(final URL _url)
    {
        this.println("onImage(" + _url + ")");
        if (this.wem != null)  {
            this.wem.onImage(_url);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPreformat(final CharSequence _text)
    {
        this.println("onPreformat(" + _text + ")");
        if (this.wem != null)  {
            this.wem.onPreformat(_text);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onText(final CharSequence _text)
    {
        this.println("onText(" + _text + ")");
        if (this.wem != null)  {
            this.wem.onText(_text);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTableOfContents(final int _deepth)
    {
        this.println("onTableOfContents(" + _deepth + ")");
        if (this.wem != null)  {
            this.wem.onTableOfContents(_deepth);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onLinkExternal(final URL _url,
                               final CharSequence _description)
    {
        this.println("onLinkExternal(" + _url + ", description=" + _description + ")");
        if (this.wem != null)  {
            this.wem.onLinkExternal(_url, _description);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLinkInternal(final CharSequence _link,
                               final CharSequence _description)
    {
        this.println("onLinkInternal(" + _link + ", description=" + _description + ")");
        if (this.wem != null)  {
            this.wem.onLinkInternal(_link, _description);
        }
    }
}
