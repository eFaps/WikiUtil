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

package org.efaps.wikiutil.parser.gwiki;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;

import org.efaps.wikiutil.wem.EProperty;
import org.efaps.wikiutil.wem.ETypeface;
import org.efaps.wikiutil.wem.IWikiEventModel;

/**
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class GWikiVisitor
{
    /**
     * Is a paragraph started?
     */
    private boolean paragraphStarted;

    /**
     * Current section level (depending on the {@link #heading}).
     *
     * @see #headingStart(EHeader)
     */
    private EHeader section = EHeader.PAGE;

    /**
     * Current heading level.
     *
     * @see #headingStart(EHeader)
     */
    private EHeader heading;

    /**
     * Is a header definition started?
     */
    private boolean headingStarted;

    /**
     * Is a table started?
     *
     * @see #newTableEntry()
     * @see #newTableRow()
     */
    private boolean tableStarted;

    /**
     * Is a table entry started?
     *
     * @see #newTableEntry()
     * @see #newTableRow()
     */
    private boolean tableEntryStarted;

    /**
     * Is a table entry paragraph started?
     *
     */
    private boolean tableEntryParagraphStarted;

    /**
     * String of current parsed text.
     */
    private StringBuilder text;

    /**
     * @see #onTypeface(ETypeface)
     */
    private final Stack<ETypeface> typefaces = new Stack<ETypeface>();

    /**
     * Indent of current list element.
     *
     * @see #listEntryStart(String, boolean)
     * @see #listEntriesEnd()
     */
    private final Stack<Integer> listIndent = new Stack<Integer>();

    /**
     * Stack used to store history which list entries are numbered.
     *
     * @see #listEntryStart(String, boolean)
     * @see #listEntriesEnd()
     */
    private final Stack<Boolean> listIsNumb = new Stack<Boolean>();

    /**
     * Related wiki event model.
     */
    private final IWikiEventModel wem;

    /**
     *
     * @param _wem  wiki event model
     */
    public GWikiVisitor(final IWikiEventModel _wem)
    {
        this.wem = _wem;
    }

    public void onProperty(final EProperty _property,
                           final String _value)
    {
        if (_property == EProperty.KEY)  {
            // must be done manually (without for) because otherwise not parsable
            final String[] myTmp = _value.trim().split(",");
            for (int idx = 0; idx < myTmp.length; idx++)  {
                this.wem.onProperty(EProperty.KEY, myTmp[idx].trim());
            }
        } else  {
            this.wem.onProperty(_property, _value.trim());
        }
    }

    public void documentStart()
    {
        this.wem.documentStart();
    }

    public void documentEnd()
    {
        makeEndTable();
        makeEndText(false);
        if (this.heading != null)  {
            headingEnd();
        }
        if (this.paragraphStarted)  {
            this.wem.paragraphEnd();
            this.paragraphStarted = false;
        }
        while (this.section != EHeader.PAGE)  {
            this.section = this.section.getParentLevel();
            this.wem.sectionEnd();
        }
        this.wem.documentEnd();
    }

    public void headingStart(final EHeader _heading)
    {
        makeEndText(false);
        if (this.paragraphStarted)  {
            this.wem.paragraphEnd();
            this.paragraphStarted = false;
        }

        if (this.heading != null)  {
            if (!this.headingStarted)  {
                this.wem.headingStart(_heading);
                if (this.text != null)  {
                    this.wem.onText(this.text.subSequence(0, this.text.length() - 1).toString().trim());
                    this.text = null;
                }
            }
            this.wem.headingEnd(_heading);
            this.headingStarted = false;
        }

        if (this.section.hasChild(_heading))  {
            while (this.section.getSubLevel() != _heading)  {
                this.section = this.section.getSubLevel();
                this.wem.sectionStart();
            }
        } else  {
            while (this.section.getSubLevel() != _heading)  {
                this.section = this.section.getParentLevel();
                this.wem.sectionEnd();
            }
        }

        this.section = _heading;
        this.wem.sectionStart();
        this.wem.headingStart(_heading);
        this.heading = _heading;
        this.headingStarted = true;
    }

    public void headingEnd()
    {
        if (this.text != null)  {
            final String curText;
            switch (this.heading)  {
                case LEVEL1:
                    curText = this.text.subSequence(0, this.text.length() - 1).toString().trim();
                    break;
                case LEVEL2:
                    curText = this.text.subSequence(0, this.text.length() - 2).toString().trim();
                    break;
                case LEVEL3:
                    curText = this.text.subSequence(0, this.text.length() - 3).toString().trim();
                    break;
                case LEVEL4:
                    curText = this.text.subSequence(0, this.text.length() - 4).toString().trim();
                    break;
                case LEVEL5:
                    curText = this.text.subSequence(0, this.text.length() - 5).toString().trim();
                    break;
                case LEVEL6:
                    curText = this.text.subSequence(0, this.text.length() - 6).toString().trim();
                    break;
                default:
                    curText = null;
            }
            if ((curText != null) && !"".equals(curText))  {
                this.wem.onText(curText);
            }
            this.text = null;
            this.wem.headingEnd(this.heading);
        }
        this.headingStarted = false;
        this.heading = null;
    }

    public void code(final String _code)
    {
        if (this.text != null)  {
            makeEndText(false);
            if (this.headingStarted)  {
                this.wem.headingEnd(this.heading);
                this.headingStarted = false;
                this.heading = null;
            }
        }
        if (!this.paragraphStarted)  {
            this.wem.paragraphStart();
            this.paragraphStarted = true;
        }
        this.wem.onPreformat(_code.trim());
    }

    public void codeEmbedded(final String _code)
    {
        startTypeface(ETypeface.CODE);
        this.text = new StringBuilder();
        this.text.append(_code);
        endTypeface(ETypeface.CODE);
/*        if (this.paragraph == null)  {
            this.paragraph = new Paragraph();
        }
        if (this.text != null)  {
            this.paragraph.add(new Text(this.text));
            this.text = null;
        }
        this.paragraph.add(new TextCode(_code));*/
    }

    /**
     *
     * @param _listEntryType    parsed text string defining the list entry (to
     *                          calculate the indent of the entry)
     * @param _isNumbered       is numbered list entry
     * @see #listIndent
     * @see #listNumber
     */
    public void listEntryStart(final String _listEntryType,
                               final boolean _isNumbered)
    {
        final int indt = StringUtils.stripEnd(_listEntryType, " \t").length();
        if (this.listIndent.isEmpty())  {
            makeEndText(true);
            this.listIndent.add(indt);
            this.listIsNumb.add(_isNumbered);
            if (_isNumbered)  {
                this.wem.listNumberedStart();
            } else  {
                this.wem.listBulletedStart();
            }
            this.wem.listEntryStart();
            this.wem.paragraphStart();
        } else  {
            makeEndText(false);
            final int curSize = this.listIndent.size();
            int parIndt = this.listIndent.peek();
            boolean prevIsNumb = false;
            while (parIndt >= indt)  {
                this.listIndent.pop();
                prevIsNumb = this.listIsNumb.pop();
                parIndt = this.listIndent.isEmpty() ? 0 : this.listIndent.peek();
            }

            this.listIndent.add(indt);
            this.listIsNumb.add(_isNumbered);

            int newSize = this.listIndent.size();
            if (newSize == curSize)  {
                this.wem.paragraphEnd();
                this.wem.listEntryEnd();
                if (prevIsNumb != _isNumbered)  {
                    if (_isNumbered)  {
                        this.wem.listBulletedEnd();
                        this.wem.listNumberedStart();
                    } else  {
                        this.wem.listNumberedEnd();
                        this.wem.listBulletedStart();
                    }
                }
                this.wem.listEntryStart();
                this.wem.paragraphStart();
            } else if (newSize > curSize)  {
                if (_isNumbered)  {
                    this.wem.listNumberedStart();
                } else  {
                    this.wem.listBulletedStart();
                }
                this.wem.listEntryStart();
                this.wem.paragraphStart();
            } else if (newSize < curSize)  {
                this.wem.paragraphEnd();
                this.wem.listEntryEnd();
                while (newSize < curSize)  {
                    this.wem.listBulletedEnd();
                    this.wem.paragraphEnd();
                    this.wem.listEntryEnd();
                    newSize++;
                }
                this.wem.listEntryStart();
                this.wem.paragraphStart();
            }
        }
    }

    public void listEntriesEnd()
    {
        makeEndText(false);
        while (!this.listIndent.isEmpty())  {
            this.listIndent.pop();
            this.wem.paragraphEnd();
            this.wem.listEntryEnd();
            this.wem.listBulletedEnd();
        }
    }

    /**
     *
     * @param _typeface
     * @see #typefaces
     */
    public void onTypeface(final ETypeface _typeface)
    {
        if (this.typefaces.contains(_typeface))  {
            endTypeface(_typeface);
        } else  {
            startTypeface(_typeface);
        }
    }

    public void startTypeface(final ETypeface _typeface)
    {
        if (this.typefaces.contains(_typeface))  {
System.err.println("type face " + _typeface + " already defined and ignored");
        } else  {
            makeEndText(false);
            if (this.tableStarted)  {
                if (!this.tableEntryStarted)  {
                    this.wem.tableEntryStart();
                    this.tableEntryStarted = true;
                }
                if (!this.tableEntryParagraphStarted)  {
                    this.wem.paragraphStart();
                    this.tableEntryParagraphStarted = true;
                }
            }
            this.typefaces.push(_typeface);
            this.wem.typefaceStart(_typeface);
        }
    }

    public void endTypeface(final ETypeface _typeface)
    {
        if (!this.typefaces.contains(_typeface))  {
System.err.println("type face " + _typeface + " not defined and ignored");
        } else  {
            makeEndText(false);
            while (this.typefaces.pop() != _typeface)  {
                this.wem.typefaceEnd(_typeface);
            }
            this.wem.typefaceEnd(_typeface);
        }
    }

    /**
     * If a paragraph within text is defined related table and texts must be
     * closed.
     */
    public void newParagraph()
    {
        makeEndTable();
        newParagraphHtml();
    }

    /**
     * New paragraph defined as HTM tag.
     */
    public void newParagraphHtml()
    {
        makeEndText(false);
        if (this.tableStarted)  {
            if (this.tableEntryParagraphStarted)  {
                this.wem.paragraphEnd();
                this.tableEntryParagraphStarted = false;
            }
        } else if (this.paragraphStarted)  {
            this.wem.paragraphEnd();
            this.paragraphStarted = false;
        }
    }

    /**
     * A new table row with a table entry is started.
     */
    public void newTableRow()
    {
        makeEndText(true);
        if (this.tableStarted)  {
            this.wem.tableRowEnd();
        } else  {
            this.wem.tableStart();
            this.wem.tableBodyStart();
            this.tableStarted = true;
        }
        this.wem.tableRowStart();
        this.tableEntryStarted = false;
    }

    /**
     * A new table entry of a table row is started. If {@link #newTableEntry()}
     * is not called before, a new table row is automatically started.
     */
    public void newTableEntry()
    {
        if (!this.tableStarted)  {
            newTableRow();
        } else  {
            makeEndText(false);
            this.wem.paragraphEnd();
            this.wem.tableEntryEnd();
            this.tableEntryStarted = false;
            this.tableEntryParagraphStarted = false;
        }
    }

    /**
     * A divider (four or more '-' in one line) is read.
     */
    public void onDivider()
    {
        makeEndText(true);
        this.wem.onDivider();
    }

    /**
     * Defines a table of contents.
     *
     * @param _depth    deepth of the table of contents
     */
    public void onToC(final String _depth)
    {
        makeEndText(true);
        Integer depth;
        try  {
            depth = Integer.parseInt(_depth);
        } catch (final NumberFormatException e)  {
            depth = 1;
        }
        if ((depth == null) || (depth < 1))  {
            depth = 1;
        } else if (depth > 6)  {
            depth = 6;
        }
        this.wem.onTableOfContents(depth);
    }

    /**
     * Link in brackets where <code>_link defines the link</code> which must be
     * proved if it is an {@link URL}.
     *
     * @param _link         URL link
     * @param _description  description
     * @see #onInternalLink(String, String)
     */
    public void onLink(final String _link,
                       final String _description)
    {
        // append external link only if  URL could be interpreted
        try  {
            final URL url = new URL(_link);
            final String description;
            if (_description != null && !"".equals(_description.trim()))  {
                description = _description.trim();
            } else  {
                description = null;
            }
            makeEndText(true);
            this.wem.onLinkExternal(url, description);
        } catch (final MalformedURLException e)  {
            onInternalLink(_link, _description);
        }
    }

    /**
     * Defines an internal link.
     *
     * @param _link         link
     * @param _description  description
     */
    public void onInternalLink(final String _link,
                               final String _description)
    {
        final String description;
        if (_description != null && !"".equals(_description.trim()))  {
            description = _description.trim();
        } else  {
            description = null;
        }
        makeEndText(true);
        this.wem.onLinkInternal(_link, description);
    }

    /**
     * Link directly defined in the GWiki (embedded within text without squared
     * brackets) which must be written as "standard" text if <code>_link</code>
     * is not an {@link URL}.
     *
     * @param _link     URL link
     */
    public void onExternalLinkText(final String _link)
    {
        URL url = null;
        try {
            url = new URL(_link);
        } catch (final MalformedURLException e) {
            url = null;
        }
        if (url == null)  {
            if (this.text == null)  {
                this.text = new StringBuilder();
            }
            this.text.append(_link);
        } else  {
            makeEndText(true);
            this.wem.onLinkExternal(url, null);
        }
    }

    public void onImage(final String _image)
    {
        URL url = null;
        try {
            url = new URL(_image);
        } catch (final MalformedURLException e) {
            url = null;
        }
        if (url == null)  {
            if (this.text == null)  {
                this.text = new StringBuilder();
            }
            this.text.append(_image);
        } else  {
            makeEndText(true);
            this.wem.onImage(url);
        }
    }

    /**
     * If {@link #tableStarted tables are started}, the Wiki events to end the
     * table are executed (by {@link #makeEndTable()}); otherwise a space is
     * appended to the {@link #text}.
     */
    public void newLine()
    {
        if (this.tableStarted)  {
            makeEndTable();
        } else  {
            if (this.text == null)  {
                this.text = new StringBuilder();
            }
            this.text.append(' ');
        }
    }

    /**
     * Appends a space to {@link #text}. If {@link #text} is <code>null</code>,
     * {@link #text} is initialized.
     *
     * @see #text
     */
    public void space()
    {
        if (this.text == null)  {
            this.text = new StringBuilder();
        }
        this.text.append(' ');
    }

    /**
     * Appends special symbol <code>_symbol</code> to {@link #text}. If
     * {@link #text} is <code>null</code> {@link #text} is initialized.
     *
     * @param _symbol   special symbol to append
     * @see #text
     */
    public void onSpecialSymbol(final String _symbol)
    {
        if (this.text == null)  {
            this.text = new StringBuilder();
        }
        this.text.append(_symbol);
    }

    public void word(final String _word)
    {
        // wiki word?
        if (_word.matches("[A-Z][a-z0-9]+([A-Z][a-z0-9]+)+"))  {
            // test for non wiki word (means wiki word starts with '!')
            if ((this.text != null) && (this.text.charAt(this.text.length() - 1) == '!'))  {
                this.text.deleteCharAt(this.text.length() - 1);
                this.text.append(_word);
            } else  {
                if (!this.headingStarted && !this.paragraphStarted)  {
                    this.wem.paragraphStart();
                    this.paragraphStarted = true;
                }
                if (this.text != null)  {
                    this.wem.onText(this.text);
                    this.text = null;
                }
                this.wem.onLinkInternal(_word, null);
            }
        } else  {
            if (this.text == null)  {
                this.text = new StringBuilder();
            }
            this.text.append(_word);
        }
    }

    /**
     * Calls the Wiki events to end a table definition.
     *
     * @see #documentEnd()
     * @see #newParagraph()
     * @see #newLine()
     */
    protected void makeEndTable()
    {
        if (this.tableStarted)  {
            makeEndText(false);
            if (this.tableEntryStarted)  {
                this.wem.paragraphEnd();
                this.wem.tableEntryEnd();
                this.tableEntryStarted = false;
            }
            this.wem.tableRowEnd();
            this.wem.tableBodyEnd();
            this.wem.tableEnd();
            this.tableStarted = false;
        }
    }

    protected void makeEndText(final boolean _startParagraph)
    {
        if (this.text != null)  {
            if (this.tableStarted)  {
                if (!this.tableEntryStarted)  {
                    this.wem.tableEntryStart();
                    this.tableEntryStarted = true;
                }
                if (!this.tableEntryParagraphStarted)  {
                    this.wem.paragraphStart();
                    this.tableEntryParagraphStarted = true;
                }
            } else if (!this.paragraphStarted && !this.headingStarted)  {
                this.wem.paragraphStart();
                this.paragraphStarted = true;
            }
            this.wem.onText(this.text);
            this.text = null;
        }
        if (_startParagraph && !this.paragraphStarted)  {
            this.wem.paragraphStart();
            this.paragraphStarted = true;
        }
    }
}
