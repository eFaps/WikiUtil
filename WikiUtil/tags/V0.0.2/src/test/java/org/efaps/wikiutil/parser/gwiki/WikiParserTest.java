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

import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.efaps.wikiutil.parser.gwiki.javacc.ParseException;
import org.efaps.wikiutil.wem.WEMDebug;
import org.efaps.wikiutil.wom.WEM2WOM;
import org.efaps.wikiutil.wom.WikiPage;
import org.efaps.wikiutil.wom.element.AbstractLineElement;
import org.efaps.wikiutil.wom.element.AbstractParagraphList;
import org.efaps.wikiutil.wom.element.Divider;
import org.efaps.wikiutil.wom.element.NewLine;
import org.efaps.wikiutil.wom.element.Paragraph;
import org.efaps.wikiutil.wom.element.Preformat;
import org.efaps.wikiutil.wom.element.Section;
import org.efaps.wikiutil.wom.element.TableOfContents;
import org.efaps.wikiutil.wom.element.list.AbstractListEntry;
import org.efaps.wikiutil.wom.element.list.ListBulleted;
import org.efaps.wikiutil.wom.element.list.ListEntry;
import org.efaps.wikiutil.wom.element.list.ListNumbered;
import org.efaps.wikiutil.wom.element.table.Table;
import org.efaps.wikiutil.wom.element.table.TableCell;
import org.efaps.wikiutil.wom.element.table.TableRow;
import org.efaps.wikiutil.wom.element.text.AbstractTypeface;
import org.efaps.wikiutil.wom.element.text.ExternalLink;
import org.efaps.wikiutil.wom.element.text.ExternalLinkWithDescription;
import org.efaps.wikiutil.wom.element.text.Image;
import org.efaps.wikiutil.wom.element.text.InternalLink;
import org.efaps.wikiutil.wom.element.text.InternalLinkWithDescription;
import org.efaps.wikiutil.wom.element.text.TextString;
import org.efaps.wikiutil.wom.element.text.TypefaceBold;
import org.efaps.wikiutil.wom.element.text.TypefaceCode;
import org.efaps.wikiutil.wom.element.text.TypefaceItalic;
import org.efaps.wikiutil.wom.property.AbstractProperty;
import org.efaps.wikiutil.wom.property.Label;
import org.efaps.wikiutil.wom.property.Sidebar;
import org.efaps.wikiutil.wom.property.Summary;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests the parser for the Google Code Wiki.
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class WikiParserTest
{
    /**
     * Tests parsing of complex definition.
     *
     * @throws ParseException is parsing of the Wiki text failed
     * @throws MalformedURLException if defined URL's could not be parsed
     */
    @Test(description = "complex definition")
    public void testComplex()
        throws ParseException, MalformedURLException
    {
        final WikiPage page = getPage("#summary summary description\n"
                + "#sidebar SIDEBAR\n"
                + "#labels abc,ttt,def\n"
                + "\n"
                + "= Heading 1 =\n"
                + "----\n"
                + "== Heading 2 ==\n"
                + "This is a text.\n"
                + "=== Heading 3 ===\n"
                + "\n"
                + "\n"
                + "==== Heading 4 ====\n"
                + "-------\n"
                + "===== Heading 5 =====\n"
                + "This is a text including refernece to image http://www/picture.png .\n"
                + "====== Heading 6 ======\n"
                + "[http://www.efaps.org eFaps]");

        final WikiPage compare = new WikiPage()
                .add(new Summary("summary description"))
                .add(new Sidebar("SIDEBAR"))
                .add(new Label("abc"))
                .add(new Label("ttt"))
                .add(new Label("def"))
                .addSubSection(new Section()
                    .addHeading(new TextString("Heading 1"))
                    .add(new Paragraph().add(new Divider()))
                    .addSubSection(new Section()
                        .addHeading(new TextString("Heading 2"))
                        .add(new Paragraph().add(new TextString("This is a text.")))
                        .addSubSection(new Section()
                            .addHeading(new TextString("Heading 3"))
                            .addSubSection(new Section()
                                .addHeading(new TextString("Heading 4"))
                                .add(new Paragraph().add(new Divider()))
                                .addSubSection(new Section()
                                    .addHeading(new TextString("Heading 5"))
                                    .add(new Paragraph()
                                        .add(new TextString("This is a text including refernece to image "))
                                        .add(new Image(new URL("http://www/picture.png")))
                                        .add(new TextString(" .")))
                                    .addSubSection(new Section()
                                        .addHeading(new TextString("Heading 6"))
                                        .add(new Paragraph()
                                            .add(new ExternalLinkWithDescription(new URL("http://www.efaps.org"),
                                                                                 "eFaps")))))))));

        checkPage(page, compare);
    }

    /**
     * Tests parsing of labels pragma.
     *
     * @throws ParseException is parsing of the Wiki text failed
     */
    @Test(description = "labels pragma")
    public void testPragmaLabels()
        throws ParseException
    {
        checkPage(
                getPage("#labels abc,ttt,def"),
                new WikiPage()
                    .add(new Label("abc"))
                    .add(new Label("ttt"))
                    .add(new Label("def")));
    }

    /**
     * Tests parsing of side bar pragma.
     *
     * @throws ParseException is parsing of the Wiki text failed
     */
    @Test(description = "side bar pragma")
    public void testPragmaSidebar()
        throws ParseException
    {
        checkPage(
                getPage("#sidebar Sidebar"),
                new WikiPage()
                    .add(new Sidebar("Sidebar")));
    }

    /**
     * Tests parsing of summary pragma.
     *
     * @throws ParseException is parsing of the Wiki text failed
     */
    @Test(description = "summary pragma")
    public void testPragmaSummary()
        throws ParseException
    {
        checkPage(
                getPage("#summary Summary"),
                new WikiPage()
                    .add(new Summary("Summary")));
    }


    /**
     * Tests parsing of multiple definitions of pragmas.
     *
     * @throws ParseException is parsing of the Wiki text failed
     */
    @Test(description = "multiple definitions of pragmas")
    public void testPragmaMultipleDefinitions()
        throws ParseException
    {
        checkPage(
                getPage("#summary Summary\n#sidebar Sidebar\n#labels abc,ttt,def"),
                new WikiPage()
                    .add(new Summary("Summary"))
                    .add(new Sidebar("Sidebar"))
                    .add(new Label("abc"))
                    .add(new Label("ttt"))
                    .add(new Label("def")));
    }


    /**
     * Test that a single heading 1 works (without new line).
     *
     * @throws ParseException is parsing of the Wiki text failed
     */
    @Test(description = "simple heading 1")
    public void testHeading1Single()
        throws ParseException
    {
        checkPage(
                getPage("= Te=st ="),
                new WikiPage()
                    .addSubSection(new Section()
                        .addHeading(new TextString("Te=st"))));
    }

    /**
     * Test that a single heading 1 works (without new line).
     *
     * @throws ParseException is parsing of the Wiki text failed
     */
    @Test(description = "simple heading 1")
    public void testHeading1Twice()
        throws ParseException
    {
        checkPage(
                getPage("= Te=st =\n=Test2="),
                new WikiPage()
                    .addSubSection(new Section()
                        .addHeading(new TextString("Te=st")))
                    .addSubSection(new Section()
                        .addHeading(new TextString("Test2"))));
    }

    /**
     * Test that a single text which starts with = works (with new line).
     *
     * @throws ParseException is parsing of the Wiki text failed
     */
    @Test(description = "text that starts with heading 1 tag but not ending with heading")
    public void testHeading1WrongWithNewLine()
        throws ParseException
    {
        checkPage(
                getPage("= Test\n"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new TextString("= Test"))));
    }

    /**
     * Test heading 1 with table.
     *
     * @throws ParseException is parsing of the Wiki text failed
     */
    @Test(description = "heading 1 with table")
    public void testHeading1WithTable()
        throws ParseException
    {
        checkPage(
                getPage("= Test =\n|| Name ||"),
                new WikiPage()
                    .addSubSection(new Section()
                        .addHeading(new TextString("Test"))
                        .add(new Paragraph()
                            .add(new Table()
                                .addBodyRow(new TableRow()
                                    .add(new TableCell()
                                        .add(new Paragraph()
                                            .add(new TextString("Name")))))))));
    }

    /**
     * Test that a single heading 2 works (without new line).
     *
     * @throws ParseException is parsing of the Wiki text failed
     */
    @Test(description = "simple heading 2")
    public void testHeading2Single()
        throws ParseException
    {
        checkPage(
                getPage("== Test =="),
                new WikiPage()
                    .addSubSection(new Section()
                        .addSubSection(new Section()
                            .addHeading(new TextString("Test")))));
    }

    /**
     * Test that a single heading 2 works (without new line).
     *
     * @throws ParseException is parsing of the Wiki text failed
     */
    @Test(description = "simple heading 2")
    public void testHeading2SingleWithBold()
        throws ParseException
    {
        checkPage(
                getPage("== *Test* =="),
                new WikiPage()
                    .addSubSection(new Section()
                        .addSubSection(new Section()
                            .addHeading(new TypefaceBold()
                                .add(new TextString("Test"))))));
    }
/*TODO correct test
    /**
     * Test that a single heading 2 works (without new line).
     *
     * @throws ParseException is parsing of the Wiki text failed

    @Test(description = "simple heading 2 with code html tag")
    public void testHeading2SingleWithCodeHTMLTag()
        throws ParseException
    {
        this.checkPage(
                this.getPage("== Test <code>code</code> =="),
                new WikiPage()
                    .addSubSection(new Section()
                        .addSubSection(new Section()
                            .addHeading(new TextString("Test <code>code</code>")))));
    }
*/

    /**
     * Test that a single text which starts with == works (with new line).
     *
     * @throws ParseException is parsing of the Wiki text failed
     */
    @Test(description = "text that starts with heading 2 tag but not ending with heading")
    public void testHeading2WrongWithNewLine()
        throws ParseException
    {
        checkPage(
                getPage("== Test\n"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new TextString("== Test"))));
    }

    /**
     * Test that a single text which starts with == works (without new line).
     *
     * @throws ParseException is parsing of the Wiki text failed
     */
    @Test(description = "wrong heading 2 interpreted as text")
    public void testHeading2WrongWithoutNewLine()
        throws ParseException
    {
        checkPage(
                getPage("== Test"),
                new WikiPage()
                    .add(new Paragraph()
                         .add(new TextString("== Test"))));
    }

    /**
     * Test that a single heading 2 works (without new line).
     *
     * @throws ParseException is parsing of the Wiki text failed
     */
    @Test(description = "simple heading 3")
    public void testHeading3Single()
        throws ParseException
    {
        checkPage(
                getPage("=== Test ==="),
                new WikiPage()
                    .addSubSection(new Section()
                        .addSubSection(new Section()
                            .addSubSection(new Section()
                                .addHeading(new TextString("Test"))))));
    }

    /**
     * Test that a single text which starts with === works (with new line).
     *
     * @throws ParseException is parsing of the Wiki text failed
     */
    @Test(description = "text that starts with heading 3 tag but not ending with heading")
    public void testHeading3WrongWithNewLine()
        throws ParseException
    {
        checkPage(
                getPage("=== Test\n"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new TextString("=== Test"))));
    }

    /**
     * Test that a single heading 4 works (without new line).
     *
     * @throws ParseException is parsing of the Wiki text failed
     */
    @Test(description = "simple heading 4")
    public void testHeading4Single()
        throws ParseException
    {
        checkPage(
                getPage("==== Test ===="),
                new WikiPage()
                    .addSubSection(new Section()
                        .addSubSection(new Section()
                            .addSubSection(new Section()
                                .addSubSection(new Section()
                                    .addHeading(new TextString("Test")))))));
    }

    /**
     * Test that a single text which starts with ==== works (with new line).
     *
     * @throws ParseException is parsing of the Wiki text failed
     */
    @Test(description = "text that starts with heading 4 tag but not ending with heading")
    public void testHeading4WrongWithNewLine()
        throws ParseException
    {
        checkPage(
                getPage("==== Test\n"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new TextString("==== Test"))));
    }

    /**
     * Test that a single heading 5 works (without new line).
     *
     * @throws ParseException is parsing of the Wiki text failed
     */
    @Test(description = "simple heading 5")
    public void testHeading5Single()
        throws ParseException
    {
        checkPage(
                getPage("===== Test ====="),
                new WikiPage()
                    .addSubSection(new Section()
                        .addSubSection(new Section()
                            .addSubSection(new Section()
                                .addSubSection(new Section()
                                    .addSubSection(new Section()
                                        .addHeading(new TextString("Test"))))))));
    }

    /**
     * Test that a single text which starts with ===== works (with new line).
     *
     * @throws ParseException is parsing of the Wiki text failed
     */
    @Test(description = "text that starts with heading 5 tag but not ending with heading")
    public void testHeading5WrongWithNewLine()
        throws ParseException
    {
        checkPage(
                getPage("===== Test\n"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new TextString("===== Test"))));
    }

    /**
     * Test that a single heading 6 works (without new line).
     *
     * @throws ParseException is parsing of the Wiki text failed
     */
    @Test(description = "simple heading 6")
    public void testHeading6Single()
        throws ParseException
    {
        checkPage(
                getPage("====== Test ======"),
                new WikiPage()
                .addSubSection(new Section()
                    .addSubSection(new Section()
                        .addSubSection(new Section()
                            .addSubSection(new Section()
                                .addSubSection(new Section()
                                    .addSubSection(new Section()
                                        .addHeading(new TextString("Test")))))))));
    }

    /**
     * Test that a single text which starts with ====== works (with new line).
     *
     * @throws ParseException is parsing of the Wiki text failed
     */
    @Test(description = "text that starts with heading 6 tag but not ending with heading")
    public void testHeading6WrongWithNewLine()
        throws ParseException
    {
        checkPage(
                getPage("====== Test\n"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new TextString("====== Test"))));
    }

    /**
     * Test that a single text witch includes a text in the syntax of 2 level
     * heading results in a text (because heading must start without pre
     * characters).
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "wrong heading 2 within text interpreted as text")
    public void testTextWithHeadingTextBetween()
        throws ParseException
    {
        checkPage(
                getPage("This is a Test with == Heading 2 == between"),
                new WikiPage()
                    .add(new Paragraph()
                         .add(new TextString("This is a Test with == Heading 2 == between"))));
    }

    /**
     * Parses a single GIF image included in a Wiki text.
     *
     * @throws ParseException if parsing of the Wiki text failed
     * @throws MalformedURLException if the image URL could not be formed
     */
    @Test(description = "single GIF image")
    public void testImageGIFWithoutText()
        throws ParseException, MalformedURLException
    {
        checkPage(getPage("http://www/picture.gif"),
                new WikiPage()
                    .add(new Paragraph()
                         .add(new Image(new URL("http://www/picture.gif")))));
    }

    /**
     * Parses a single JPG image included in a Wiki text.
     *
     * @throws ParseException if parsing of the Wiki text failed
     * @throws MalformedURLException if the image URL could not be formed
     */
    @Test(description = "single JPEG image (with .jpg as extension")
    public void testImageJPGWithoutText()
        throws ParseException, MalformedURLException
    {
        checkPage(
                getPage("http://www/picture.jpg"),
                new WikiPage()
                    .add(new Paragraph()
                         .add(new Image(new URL("http://www/picture.jpg")))));
    }

    /**
     * Parses a single JPEG image included in a Wiki text.
     *
     * @throws ParseException if parsing of the Wiki text failed
     * @throws MalformedURLException if the image URL could not be formed
     */
    @Test(description = "single JPEG image (with .jpeg as extension")
    public void testImageJPEGWithoutText()
        throws ParseException, MalformedURLException
    {
        checkPage(
                getPage("http://www/picture.jpeg"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new Image(new URL("http://www/picture.jpeg")))));
    }

    /**
     * Parses a single PNG image included in a Wiki text.
     *
     * @throws ParseException if parsing of the Wiki text failed
     * @throws MalformedURLException if the image URL could not be formed
     */
    @Test(description = "single PNG image")
    public void testImagePNGWithoutText()
        throws ParseException, MalformedURLException
    {
        checkPage(
                getPage("http://www/picture.png"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new Image(new URL("http://www/picture.png")))));
    }

    /**
     * Parses a link URL included in a Wiki text.
     *
     * @throws ParseException if parsing of the Wiki text failed
     * @throws MalformedURLException if the image URL could not be formed
     */
    @Test(description = "parse external link embedded within text")
    public void testExternalLinkWithoutText()
        throws ParseException, MalformedURLException
    {
        checkPage(
                getPage("http://www/picture.html"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new ExternalLink(new URL("http://www/picture.html")))));
    }

    /**
     * Parses an external link without description within squared brackets in a
     * Wiki text.
     *
     * @throws ParseException if parsing of the Wiki text failed
     * @throws MalformedURLException if the image URL could not be formed
     */
    @Test(description = "parse external link in squared bracket without description")
    public void testExternalLinkSquaredBracketWithoutTextWithoutDescription()
        throws ParseException, MalformedURLException
    {
        checkPage(
                getPage("[http://www/picture.html]"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new ExternalLink(new URL("http://www/picture.html")))));
    }

    /**
     * Parses an external link with description within squared brackets in a
     * Wiki text.
     *
     * @throws ParseException if parsing of the Wiki text failed
     * @throws MalformedURLException if the image URL could not be formed
     */
    @Test(description = "parse external link in squared bracket with description")
    public void testExternalLinkSquaredBracketWithoutTextWithDescription()
        throws ParseException, MalformedURLException
    {
        checkPage(
                getPage("[http://www/picture.html a simple text]"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new ExternalLinkWithDescription(new URL("http://www/picture.html"), "a simple text"))));
    }

    /**
     * Parses an internal link without description within squared brackets in
     * a Wiki text.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "internal link within squared brackets without description")
    public void testInternalLinkSquaredBracketWithoutTextWithoutDescription()
        throws ParseException
    {
        checkPage(
                getPage("[Introduction]"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new InternalLink("Introduction"))));
    }

    /**
     * Parses an internal link wit description within squared brackets in
     * a Wiki text.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "internal link within squared brackets with description")
    public void testInternalLinkSquaredBracketWithoutTextWithDescription()
        throws ParseException
    {
        checkPage(
                getPage("[Introduction  a simple text]"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new InternalLinkWithDescription("Introduction", "a simple text"))));
    }

    /**
     * Tests parsing of one single bulleted list entry.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "bulleted list with one entry")
    public void testListBulletedOneEntry()
        throws ParseException
    {
        checkPage(
                getPage(" * this is an entry"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new ListBulleted()
                            .add(new ListEntry()
                                .add(new Paragraph()
                                    .add(new TextString("this is an entry")))))));
    }

    /**
     * Tests that spaces in front of bulleted list entries are ignored.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "bulleted list with one entry, but with multiple spaces")
    public void testListBulletedOneEntryMultipleSpaces()
        throws ParseException
    {
        checkPage(
                getPage("   *  this is an entry"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new ListBulleted()
                            .add(new ListEntry()
                                .add(new Paragraph()
                                    .add(new TextString("this is an entry")))))));
    }

    /**
     * Test bulleted list with two entries.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "bulleted list with two entries")
    public void testListBulletedTwoEntries()
        throws ParseException
    {
        checkPage(
                getPage(" * this is first entry\n * this is second entry"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new ListBulleted()
                            .add(new ListEntry()
                                .add(new Paragraph()
                                    .add(new TextString("this is first entry"))))
                            .add(new ListEntry()
                                .add(new Paragraph()
                                    .add(new TextString("this is second entry")))))));
    }

    /**
     * Tests parsing bulleted list with two entries.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "bulleted list with two entries")
    public void testListBulletedTwoEntriesParagraphDivider()
        throws ParseException
    {
        checkPage(
                getPage(" * this is first entry\n * this is second entry\n\n----"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new ListBulleted()
                            .add(new ListEntry()
                                .add(new Paragraph()
                                    .add(new TextString("this is first entry"))))
                            .add(new ListEntry()
                                .add(new Paragraph()
                                    .add(new TextString("this is second entry"))))))
                    .add(new Paragraph()
                        .add(new Divider())));
    }

    /**
     * Test bulleted list with deep entries.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "bulleted list with deep entries")
    public void testListBulletedMultipleDeepEntries()
        throws ParseException
    {
        checkPage(
                getPage(
                          "  * this is entry 1\n"
                        + "    *  this is entry 1a\n"
                        + "    * this is entry 1b\n"
                        + "   * this is entry 1c\n"
                        + " * this is entry 2"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new ListBulleted()
                            .add(new ListEntry()
                                .add(new Paragraph()
                                    .add(new TextString("this is entry 1"))
                                    .add(new ListBulleted()
                                        .add(new ListEntry()
                                            .add(new Paragraph().add(new TextString("this is entry 1a"))))
                                        .add(new ListEntry()
                                            .add(new Paragraph().add(new TextString("this is entry 1b"))))
                                        .add(new ListEntry()
                                            .add(new Paragraph().add(new TextString("this is entry 1c")))))))
                            .add(new ListEntry()
                                .add(new Paragraph()
                                    .add(new TextString("this is entry 2")))))));

    }
/* TODO correct not working test
    @Test(description = "")
    public void testListBulletedHTML()
        throws ParseException
    {
        this.checkPage(
                this.getPage(
                          "<ul>text"
                            + "<li>this is entry 1"
                                + "<ul>"
                                    + "<li>this is entry 1a</li>"
                                    + "<li>this is entry 1b</li>"
                                    + "<li>this is entry 1c</li>"
                                + "</ul></li>"
                            + "<li>this is entry 2</li>"
                        + "</ul>"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new TextString("text"))
                        .add(new ListBulleted()
                            .add(new ListEntry()
                                .add(new Paragraph()
                                    .add(new TextString("this is entry 1"))
                                    .add(new ListBulleted()
                                        .add(new ListEntry()
                                            .add(new Paragraph()
                                                .add(new TextString("this is entry 1a"))))
                                        .add(new ListEntry()
                                            .add(new Paragraph()
                                                .add(new TextString("this is entry 1b"))))
                                        .add(new ListEntry()
                                            .add(new Paragraph()
                                                .add(new TextString("this is entry 1c")))))))
                            .add(new ListEntry()
                                .add(new Paragraph()
                                    .add(new TextString("this is entry 2")))))));
    }
*/
    /**
     * Tests parsing of one single numbered list entry.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "numbered list with one entry")
    public void testListNumberedOneEntry()
        throws ParseException
    {
        checkPage(
                getPage(" # this is an entry"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new ListNumbered()
                            .add(new ListEntry()
                                .add(new Paragraph()
                                    .add(new TextString("this is an entry")))))));
    }

    /**
     * Tests parsing of mixed lists.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "mixed list with deep entries")
    public void testListMixedMultipleDeepEntries()
        throws ParseException
    {
        checkPage(
                getPage(
                          "  * this is entry 1\n"
                        + "    *  this is entry 1a\n"
                        + "    * this is entry 1b\n"
                        + "   * this is entry 1c\n"
                        + "   # this is entry 1d\n"
                        + "   # this is entry 1e\n"
                        + " * this is entry 2\n"
                        + " # this is new list entry 1"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new ListBulleted()
                            .add(new ListEntry()
                                .add(new Paragraph()
                                    .add(new TextString("this is entry 1"))
                                    .add(new ListBulleted()
                                        .add(new ListEntry()
                                            .add(new Paragraph().add(new TextString("this is entry 1a"))))
                                        .add(new ListEntry()
                                            .add(new Paragraph().add(new TextString("this is entry 1b"))))
                                        .add(new ListEntry()
                                            .add(new Paragraph().add(new TextString("this is entry 1c")))))
                                    .add(new ListNumbered()
                                        .add(new ListEntry()
                                            .add(new Paragraph().add(new TextString("this is entry 1d"))))
                                        .add(new ListEntry()
                                            .add(new Paragraph().add(new TextString("this is entry 1e")))))))
                            .add(new ListEntry()
                                .add(new Paragraph().add(new TextString("this is entry 2")))))
                        .add(new ListNumbered()
                            .add(new ListEntry()
                                .add(new Paragraph().add(new TextString("this is new list entry 1")))))));
    }

    /**
     * Tests parsing of list entry, text, list entry.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "parsing of list entry, text, list entry")
    public void testListTextList()
        throws ParseException
    {
        checkPage(
                getPage(" * list 1\ntext\n * list 2"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new ListBulleted()
                            .add(new ListEntry()
                                .add(new Paragraph()
                                    .add(new TextString("list 1")))))
                        .add(new TextString("text"))
                        .add(new ListBulleted()
                            .add(new ListEntry()
                                .add(new Paragraph()
                                    .add(new TextString("list 2")))))));
    }

    /**
     * Test parsing list entry with link.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "parsing list entry with link")
    public void testListWithLink()
        throws ParseException
    {
        checkPage(
                getPage(" * [Start Introduction]"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new ListBulleted()
                            .add(new ListEntry()
                                .add(new Paragraph()
                                        .add(new InternalLinkWithDescription("Start", "Introduction")))))));
    }

    /**
     * Checks multi line text that only one text element is included.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "texts in two lines (must be one text)")
    public void testMultilineText()
        throws ParseException
    {
        checkPage(
                getPage("abcdef\nhijkl"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new TextString("abcdef hijkl"))));
    }

    /**
     * Tests that a paragraph is created within text.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "texts in two lines with empty line between (must be two texts)")
    public void testParagraphHtml()
        throws ParseException
    {
        checkPage(
                getPage("abcdef<p>geha</p>"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new TextString("abcdef")))
                    .add(new Paragraph()
                        .add(new TextString("geha"))));
    }

    /**
     * Tests that a paragraph is created within text.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "texts in two lines with empty line between (must be two texts)")
    public void testParagraphText()
        throws ParseException
    {
        checkPage(
                getPage("abcdef\n\ngeha"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new TextString("abcdef")))
                    .add(new Paragraph()
                        .add(new TextString("geha"))));
    }

    /**
     * Test a paragraph is not created after an heading.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "two texts with heading 1")
    public void testParagraphTextWithHeading()
        throws ParseException
    {
        checkPage(
                getPage("= Heading =\n\nabcdef\n\ngeha"),
                new WikiPage()
                    .addSubSection(new Section()
                        .addHeading(new TextString("Heading"))
                        .add(new Paragraph()
                            .add(new TextString("abcdef")))
                        .add(new Paragraph()
                            .add(new TextString("geha")))));
    }

    /**
     * Checks that comments ignored.
     *
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "comment")
    public void testComment()
        throws ParseException
    {
        checkPage(
                getPage("<wiki:comment>\nMy Comment\n</wiki:comment>"),
                new WikiPage());
    }


    /**
     * Complex check that comments ignored.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "complex check that comments ignored")
    public void testCommentComplex()
        throws ParseException
    {
        checkPage(
                getPage("Before Comment <wiki:comment>\nMy Comment\n</wiki:comment>and after Comment"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new TextString("Before Comment and after Comment"))));
    }

    /**
     * Checks that code could be parsed.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "complete code lines")
    public void testCodeText()
        throws ParseException
    {
        checkPage(
                getPage("{{{\nabcdef\r\ngeha\n  dies\n}}}"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new Preformat("abcdef\ngeha\n  dies"))));
    }

    /**
     * Checks that code could be parsed.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "complete code lines")
    public void testCodeHTML()
        throws ParseException
    {
        checkPage(
                getPage("<code>\nabcdef\r\ngeha\n  dies\n</code>"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new Preformat("abcdef\ngeha\n  dies".trim()))));
    }

    /**
     * Checks that a code with Windows new lines could be parsed.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "complete code lines with windows new lines")
    public void testCodeWindows()
        throws ParseException
    {
        checkPage(
                getPage("{{{\r\n\r\nabcdef\r\ngeha\r\n  dies\r\n}}}"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new Preformat("abcdef\ngeha\n  dies"))));
    }

    /**
     * Checks that embedded code within text could be parsed.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "embedded code within text")
    public void testCodeTextInline()
        throws ParseException
    {
        checkPage(
                getPage("text start {{{some code}}} and text end"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new TextString("text start "))
                        .add(new TypefaceCode()
                            .add(new TextString("some code")))
                        .add(new TextString(" and text end"))));
    }

    /**
     * Checks that embedded code within text with new lines could be parsed.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "embedded code within text with new lines")
    public void testCodeTextInlineInNewLine()
        throws ParseException
    {
        checkPage(
                getPage("text start\n{{{some code}}}\nand text end"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new TextString("text start "))
                        .add(new TypefaceCode()
                            .add(new TextString("some code")))
                        .add(new TextString(" and text end"))));
    }

    /**
     * Checks embedded code within text and new text in new line.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "embedded code within text and new text in new line")
    public void testCodeWithinTextAndTextInNewLine()
        throws ParseException
    {
        checkPage(
                getPage("text start {{{some code}}} and text end\n\nnew line"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new TextString("text start "))
                        .add(new TypefaceCode()
                            .add(new TextString("some code")))
                        .add(new TextString(" and text end")))
                    .add(new Paragraph()
                         .add(new TextString("new line"))));
    }

    /**
     * Tests parsing of a single divider.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "single divider")
    public void testDividerSingle()
        throws ParseException
    {
        checkPage(
                getPage("----"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new Divider())));
    }

    /**
     * Tests parsing of a single divider with many minus.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "single divider with many minus")
    public void testDividerSingleWithManyMinus()
        throws ParseException
    {
        checkPage(
                getPage("---------"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new Divider())));
    }

    /**
     * Tests parsing of wrong divider embedded within text.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "wrong divider embedded within text")
    public void testDividerWrongInline()
        throws ParseException
    {
        checkPage(
                getPage("A text with divider ---- tag"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new TextString("A text with divider ---- tag"))));
    }

    /**
     * Tests table of contents with unparsable maximum depth.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "table of contents with unparsable maximum depth")
    public void testTOCDepthUnparsable()
        throws ParseException
    {
        checkPage(
                getPage("<wiki:toc max_depth=\"A\"/>"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new TableOfContents(1))));
    }

    /**
     * Tests table of contents with maximum depth greater than 6.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "table of contents with maximum depth greater than 6")
    public void testTOCGreaterThan6()
        throws ParseException
    {
        checkPage(
                getPage("<wiki:toc max_depth=\"7\"/>"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new TableOfContents(6))));
    }

    /**
     * Tests table of contents with maximum depth lower than 1.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "table of contents with maximum depth lower than 1")
    public void testTOCLowerThan1()
        throws ParseException
    {
        checkPage(
                getPage("<wiki:toc max_depth=\"0\"/>"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new TableOfContents(1))));
    }

    /**
     * Tests that a table of contents could be parsed.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "simple table of contents")
    public void testTOCSingle()
        throws ParseException
    {
        checkPage(
                getPage("<wiki:toc max_depth=\"1\"/>"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new TableOfContents(1))));
    }

    /**
     * Tests that the max_depth attribute is correctly read.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "max_depth attribute is read as text")
    public void testTOCWrongOnlyMaxDepthText()
        throws ParseException
    {
        checkPage(
                getPage("wiki:toc max_depth=\"1\""),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new TextString("wiki:toc max_depth=\"1\""))));
    }

    /**
     * Tests that a table of contents with multiple spaces could be parsed.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "table of contents with multiple spaces")
    public void testTOCWithSpaces()
        throws ParseException
    {
        checkPage(
                getPage("<wiki:toc  \tmax_depth = \"1\" />"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new TableOfContents(1))));
    }

    /**
     * Tests a table of contents with text in the same line.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "table of contents with text at the end in the same line")
    public void testTOCWithTextAtEnd()
        throws ParseException
    {
        checkPage(
                getPage("<wiki:toc max_depth=\"1\" />abcd\ngeha"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new TableOfContents(1))
                        .add(new TextString("abcd geha"))));
    }

    /**
     * Test that a table of contents within a text could be parsed (and
     * interpreted).
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "table of contents at the beginning in the same line")
    public void testTOCWithTextAtStart()
        throws ParseException
    {
        checkPage(
                getPage("abcd<wiki:toc max_depth=\"1\" />"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new TextString("abcd"))
                        .add(new TableOfContents(1))));
    }

    /**
     * Tests two bold statements within a text.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "two bold statements within a text")
    public void testTypefaceBoldText()
        throws ParseException
    {
        checkPage(
                getPage("Hello * Bold * and a second * Bold *"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new TextString("Hello "))
                        .add(new TypefaceBold()
                            .add(new TextString(" Bold ")))
                        .add(new TextString(" and a second "))
                        .add(new TypefaceBold()
                            .add(new TextString(" Bold ")))));
    }

    /**
     * Tests two italic statements within a text.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "two italic statements within a text")
    public void testTypefaceItalicText()
        throws ParseException
    {
        checkPage(
                getPage("Hello _Italic_ and a second _ Italic _"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new TextString("Hello "))
                        .add(new TypefaceItalic()
                            .add(new TextString("Italic")))
                        .add(new TextString(" and a second "))
                        .add(new TypefaceItalic()
                            .add(new TextString(" Italic ")))));
    }

    /**
     * Tests bold with include sorce code.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "bold with include sorce code")
    public void testTypefaceBoldTextWithTypefaceCode()
        throws ParseException
    {
        checkPage(
                getPage("This is a *{{{test}}}* for two type faces"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new TextString("This is a "))
                        .add(new TypefaceBold()
                            .add(new TypefaceCode()
                                .add(new TextString("test"))))
                        .add(new TextString(" for two type faces"))));
    }

    /**
     * Tests parsing of a table with one row and two entries.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "table with one row and two entries")
    public void testTableOneRow()
        throws ParseException
    {
        checkPage(
                getPage("|| First || Second ||"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new Table()
                            .addBodyRow(new TableRow()
                                .add(new TableCell()
                                    .add(new Paragraph()
                                        .add(new TextString("First"))))
                                .add(new TableCell()
                                    .add(new Paragraph()
                                        .add(new TextString("Second"))))))));
    }

    /**
     * Tests parsing of a table with one row and two bold entries.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "table with one row and two bold entries")
    public void testTableOneRowWithBoldEntries()
        throws ParseException
    {
        checkPage(
                getPage("|| *First* || *Second* ||"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new Table()
                            .addBodyRow(new TableRow()
                                .add(new TableCell()
                                    .add(new Paragraph()
                                        .add(new TypefaceBold()
                                            .add(new TextString("First")))))
                                .add(new TableCell()
                                    .add(new Paragraph()
                                        .add(new TypefaceBold()
                                            .add(new TextString("Second")))))))));
    }

    /**
     * Tests parsing of a table with one row and two text string in two lines.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "table with one row and two text string in two lines")
    public void testTableOneRowWithHtmlParagraphEntries()
        throws ParseException
    {
        checkPage(
                getPage("|| First Line<p>Second Line</p> ||"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new Table()
                            .addBodyRow(new TableRow()
                                .add(new TableCell()
                                    .add(new Paragraph()
                                        .add(new TextString("First Line")))
                                    .add(new Paragraph()
                                        .add(new TextString("Second Line"))))))));
    }

    /**
     * Tests parsing of a table with one row and two entries with post
     * paragraph.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "table with one row and two entries with post paragraph")
    public void testTableOneRowWithPostParagraphText()
        throws ParseException
    {
        checkPage(
                getPage("|| First || Second ||\n\npost text"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new Table()
                            .addBodyRow(new TableRow()
                                .add(new TableCell()
                                    .add(new Paragraph()
                                        .add(new TextString("First"))))
                                .add(new TableCell()
                                    .add(new Paragraph()
                                        .add(new TextString("Second")))))))
                    .add(new Paragraph()
                        .add(new TextString("post text"))));
    }

    /**
     * Tests parsing of a table with one row and two entries with pre and post
     * text line.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "table with one row and two entries with pre and post text line")
    public void testTableOneRowWithPrePostText()
        throws ParseException
    {
        checkPage(
                getPage("pre text\n|| First || Second ||\npost text"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new TextString("pre text"))
                        .add(new Table()
                            .addBodyRow(new TableRow()
                                .add(new TableCell()
                                    .add(new Paragraph()
                                        .add(new TextString("First"))))
                                .add(new TableCell()
                                    .add(new Paragraph()
                                        .add(new TextString("Second"))))))
                        .add(new TextString("post text"))));
    }

    /**
     * Tests parsing of a table with three rows and two columns.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "table with three rows and two columns")
    public void testTableThreeRows()
        throws ParseException
    {
        checkPage(
                getPage("|| A1 || B1 ||\n|| A2 || B2 ||\n|| A3 || B3 ||"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new Table()
                            .addBodyRow(new TableRow()
                                .add(new TableCell()
                                    .add(new Paragraph()
                                        .add(new TextString("A1"))))
                                .add(new TableCell()
                                    .add(new Paragraph()
                                        .add(new TextString("B1")))))
                            .addBodyRow(new TableRow()
                                .add(new TableCell()
                                    .add(new Paragraph()
                                        .add(new TextString("A2"))))
                                .add(new TableCell()
                                    .add(new Paragraph()
                                        .add(new TextString("B2")))))
                            .addBodyRow(new TableRow()
                                .add(new TableCell()
                                    .add(new Paragraph()
                                        .add(new TextString("A3"))))
                                .add(new TableCell()
                                    .add(new Paragraph()
                                        .add(new TextString("B3"))))))));
    }

    /**
     * Tests parsing of a not correct defined table row.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "not correct definition of a table row")
    public void testTableWrongSyntax()
        throws ParseException
    {
// TODO: ev. parse exception?
        checkPage(
                getPage("abc || First || Second ||"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new TextString("abc"))
                        .add(new Table()
                            .addBodyRow(new TableRow()
                                .add(new TableCell()
                                    .add(new Paragraph()
                                        .add(new TextString("First"))))
                                .add(new TableCell()
                                    .add(new Paragraph()
                                        .add(new TextString("Second"))))))));
    }

    /**
     * Tests parsing of a Wiki word as internal link.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "correct parsed Wiki word as internal link")
    public void testWikiWord()
        throws ParseException
    {
        checkPage(
                getPage("AutoLink"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new InternalLink("AutoLink"))));
    }

    /**
     * Tests parsing of a Wiki word as internal link.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "correct parsed Wiki word as internal link with double upper case letters and number")
    public void testWikiWordComplex()
        throws ParseException
    {
        checkPage(
                getPage("AutoLink4AnExmaple"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new InternalLink("AutoLink4AnExmaple"))));
    }

    /**
     * Tests parsing of a wrong Wiki word embedded as text.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "wrong Wiki word with prefix text")
    public void testWikiWordWrongPrefixText()
        throws ParseException
    {
        checkPage(
                getPage("aAutoLink"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new TextString("aAutoLink"))));
    }

    /**
     * Tests parsing of a wrong Wiki word embedded as text.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "wrong Wiki word with suffix text")
    public void testWikiWordWrongSuffixText()
        throws ParseException
    {
        checkPage(
                getPage("AutoLinkFadG ssd"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new TextString("AutoLinkFadG ssd"))));
    }

    /**
     * Tests parsing of a Wiki word with separator and text.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "Wiki word with separator + and text")
    public void testWikiWordWithSeparatorPlusWithText()
        throws ParseException
    {
        checkPage(
                getPage("AutoLink12+23"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new InternalLink("AutoLink12"))
                        .add(new TextString("+23"))));
    }

    /**
     * Tests parsing of a Wiki word with separator and text.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "Wiki word with separator - and text")
    public void testWikiWordWithSeparatorMinusWithText()
        throws ParseException
    {
        checkPage(
                getPage("AutoLink12-23"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new InternalLink("AutoLink12"))
                        .add(new TextString("-23"))));
    }

    /**
     * Tests parsing of a Wiki word with separator and text.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "Wiki word with separator = and text")
    public void testWikiWordWithSeparatorEqualWithText()
        throws ParseException
    {
        checkPage(
                getPage("AutoLink12=23"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new InternalLink("AutoLink12"))
                        .add(new TextString("=23"))));
    }

    /**
     * Test parsing of new line.
     *
     * @throws ParseException if parsing failed
     */
    @Test(description = "test new line with HTML tag br")
    public void testNewLineHtml()
        throws ParseException
    {
        checkPage(
                getPage("text 1<br/>text 2"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new TextString("text 1"))
                        .add(new NewLine())
                        .add(new TextString("text 2"))));
    }

    /**
     * Tests parsing of a non Wiki word.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "wrong Wiki word embedded text")
    public void testNonWikiWord()
        throws ParseException
    {
        checkPage(
                getPage("!AutoLink12+23"),
                new WikiPage()
                    .add(new Paragraph()
                        .add(new TextString("AutoLink12+23"))));
    }

    /**
     * Tests parsing of complex example.
     *
     * @throws ParseException if parsing of the Wiki text failed
     */
    @Test(description = "complex text from real 'world'")
    public void testRealTextExample()
        throws ParseException
    {
        final StringReader reader = new StringReader(
                  "#summary describes the methodic how configuration items could be used within MX\n"
                + "#sidebar SIDEBAR\n"
                + "#labels abc,def,ttt\n"
                + "\n"
                + "= Introduction =\n"
                + "== Configuration Items 1 ==\n"
                + "\n"
                + "Currently == Cond == within a lot of MX projects the existing configuration (attributes, types, "
                        + "also commands etc.) are handled via MQL update scripts. One good example for that are the "
                        + "installation scripts which are delivered with the installation packages of the different "
                        + "centrals.\n"
                + "\n"
                + "----\n"
                + "== Configuration Items ==\n"
                + "As describes in [http://www.wikipedia.org Wikipedia] the term "
                        + "[http://en.wikipedia.org/wiki/Configuration_item Configuration Items] refers to the "
                        + "fundamental structural unit of a configuration management system. In MX the fundamental "
                        + "structure unit are e.g. for the data model attributes, types, policies..., for the user "
                        + "interface e.g. commands, menus, web forms and web tables. So the basic idea behind the "
                        + "\"Configuration Item\" methodic is to store a complete description of a fundamental "
                        + "structure unit in one single file. This means that each file could be handled easily "
                        + "within a source code repository (like [http://subversion.tigris.org/ Subversion] etc.).\n"
                + "-----\n"
                + "\n"
                + "Instead of described each versions and a delta (like in the XML update files delivered from MX) the "
                        + "idea behind the method is to describe the target of an administration object. E.g. for a "
                        + "command the HRef, all settings and all properties are described in this file. If the "
                        + "command must be changed a developer could see the complete command description. A "
                        + "modification of the command is for the developer than very easy. If then the command is "
                        + "deployed the existing command in MX is updated to the new target description in the "
                        + "file.\n");
        GWikiParser.parse(reader);
    }

    /**
     * Parses given <code>_wikiCode</code> and returns the parsed Wiki page.
     *
     * @param _wikiCode     google Wiki code to parse
     * @return parsed Wiki page
     * @throws ParseException if parsing of the Wiki text failed
     */
    private WikiPage getPage(final String _wikiCode)
        throws ParseException
    {
        final WEM2WOM wem = new WEM2WOM();
        GWikiParser.parse(new WEMDebug(System.out, wem), new StringReader(_wikiCode));
        return wem.getPage();
    }

    /**
     * Checks that <code>_page</code> and <code>_compare</code> are equal.
     *
     * @param _page     parsed page to check
     * @param _compare  expected page
     */
    private void checkPage(final WikiPage _page,
                           final WikiPage _compare)
    {
        Assert.assertEquals(_page.getProperties().size(),
                            _compare.getProperties().size(),
                            "check size of properties");
        for (int idx = 0; idx < _page.getProperties().size(); idx++)  {
            final AbstractProperty pagePragma = _page.getProperties().get(idx);
            final AbstractProperty compPragma = _compare.getProperties().get(idx);
            Assert.assertEquals(pagePragma.getClass(),
                                compPragma.getClass(),
                                "compare pragma class");
            Assert.assertEquals(pagePragma.getValue(),
                                compPragma.getValue(),
                                "compare value of property");
        }

        // check paragraphs
        checkParagraphs("", _page, _compare);

        // check sub sections
        Assert.assertEquals(_page.getSubSections().size(),
                            _compare.getSubSections().size(),
                            "same # of sub sections 1");
        for (int jdx = 0; jdx < _page.getSubSections().size(); jdx++)  {
            checkSection("" + jdx,
                              _page.getSubSections().get(jdx),
                              _compare.getSubSections().get(jdx));
        }
    }

    /**
     *
     * @param _path         current checked path
     * @param _pageSection  section of the original page to check
     * @param _compSection  expected section values
     */
    private void checkSection(final String _path,
                              final Section _pageSection,
                              final Section _compSection)
    {
        // check headings
        Assert.assertEquals(_pageSection.getHeadings().size(),
                            _compSection.getHeadings().size(),
                            "same # of headings " + _path);
        for (int jdx = 0; jdx < _pageSection.getHeadings().size(); jdx++)  {
            checkLineElement(_path + "-" + jdx,
                                  _pageSection.getHeadings().get(jdx),
                                  _compSection.getHeadings().get(jdx));
        }
        // check paragraphs
        checkParagraphs(_path, _pageSection, _compSection);
        // check sub sections
        Assert.assertEquals(_pageSection.getSubSections().size(),
                            _compSection.getSubSections().size(),
                            "same # of sub sections 2 " + _path);
        for (int jdx = 0; jdx < _pageSection.getSubSections().size(); jdx++)  {
            checkSection(_path + "-" + jdx,
                              _pageSection.getSubSections().get(jdx),
                              _compSection.getSubSections().get(jdx));
        }
    }

    /**
     * Checks that the <code>_pageTextLine</code> is equal to the
     * <code>_compTextLine</code>.
     *
     * @param _path         current checked path
     * @param _pageTextLine text line from the original page
     * @param _compTextLine expected text line
     */
    private void checkParagraphs(final String _path,
                                 final AbstractParagraphList<?> _pageTextLine,
                                 final AbstractParagraphList<?> _compTextLine)
    {
        Assert.assertEquals(_pageTextLine.getParagraphs().size(),
                            _compTextLine.getParagraphs().size(),
                            "compare text elements size for element " + _path
                                    + " (" + _pageTextLine.getParagraphs() + ")");
        for (int jdx = 0; jdx < _pageTextLine.getParagraphs().size(); jdx++)  {
            final Paragraph pagePara = _pageTextLine.getParagraphs().get(jdx);
            final Paragraph compPara = _compTextLine.getParagraphs().get(jdx);
            Assert.assertEquals(pagePara.getClass(),
                                compPara.getClass(),
                                "check text classes for element " + _path + "-" + jdx);
            Assert.assertEquals(pagePara.getElements().size(),
                                compPara.getElements().size(),
                                "check size for element " + _path + "-" + jdx + " (" + _pageTextLine + ")");
            for (int kdx = 0; kdx < pagePara.getElements().size(); kdx++)  {
                checkLineElement(_path + "-" + jdx + "-" + kdx,
                                      pagePara.getElements().get(kdx),
                                      compPara.getElements().get(kdx));
            }
        }
    }

    /**
     *
     * @param _path         current checked path
     * @param _pageElem     element from the original page
     * @param _compElem     expected element
     */
    private void checkLineElement(final String _path,
                                  final AbstractLineElement _pageElem,
                                  final AbstractLineElement _compElem)
    {
        Assert.assertEquals(_pageElem.getClass(),
                            _compElem.getClass(),
                            "check equal class for " + _path);
        if (_pageElem instanceof Preformat)  {
            Assert.assertEquals(((Preformat) _pageElem).getCode(),
                                 ((Preformat) _compElem).getCode(),
                                 "check code texts for element " + _path);
        } else if (_pageElem instanceof ExternalLink)  {
            Assert.assertEquals(((ExternalLink) _pageElem).getURL(),
                                ((ExternalLink) _pageElem).getURL(),
                                "check external link url for element " + _path);
        } else if (_pageElem instanceof ExternalLinkWithDescription)  {
            Assert.assertEquals(((ExternalLinkWithDescription) _pageElem).getURL(),
                                ((ExternalLinkWithDescription) _pageElem).getURL(),
                                "check external link url for element " + _path);
            Assert.assertEquals(((ExternalLinkWithDescription) _pageElem).getDescription(),
                                ((ExternalLinkWithDescription) _pageElem).getDescription(),
                                "check external link description for element " + _path);
        } else if (_pageElem instanceof AbstractListEntry<?>)  {
            Assert.assertEquals(((AbstractListEntry<?>) _pageElem).getEntries().size(),
                                ((AbstractListEntry<?>) _compElem).getEntries().size(),
                                "same # of entries for list element " + _path);
            for (int ldx = 0; ldx < ((AbstractListEntry<?>) _pageElem).getEntries().size(); ldx++)  {
                checkParagraphs(_path + "-" + ldx,
                                     ((AbstractListEntry<?>) _pageElem).getEntries().get(ldx),
                                     ((AbstractListEntry<?>) _compElem).getEntries().get(ldx));
            }
        } else if (_pageElem instanceof AbstractTypeface<?>)  {
            Assert.assertEquals(_pageElem.getClass(),
                                _compElem.getClass(),
                                "same class for type face element " + _path);
            Assert.assertEquals(((AbstractTypeface<?>) _pageElem).getElements().size(),
                                ((AbstractTypeface<?>) _compElem).getElements().size(),
                                "same # of entries for type face element " + _path);
            for (int ldx = 0; ldx < ((AbstractTypeface<?>) _pageElem).getElements().size(); ldx++)  {
                checkLineElement(_path + "-" + ldx,
                                      ((AbstractTypeface<?>) _pageElem).getElements().get(ldx),
                                      ((AbstractTypeface<?>) _compElem).getElements().get(ldx));
            }
        } else if (_pageElem instanceof Image)  {
            Assert.assertEquals(((Image) _pageElem).getURL(),
                                ((Image) _compElem).getURL(),
                                "check image url for element " + _path);
        } else if (_pageElem instanceof InternalLink)  {
            Assert.assertEquals(((InternalLink) _pageElem).getLink(),
                                ((InternalLink) _compElem).getLink(),
                                "check internal link for element " + _path);
        } else if (_pageElem instanceof InternalLinkWithDescription)  {
            Assert.assertEquals(((InternalLinkWithDescription) _pageElem).getLink(),
                                ((InternalLinkWithDescription) _pageElem).getLink(),
                                "check internal link for element " + _path);
            Assert.assertEquals(((InternalLinkWithDescription) _pageElem).getDescription(),
                                ((InternalLinkWithDescription) _pageElem).getDescription(),
                                "check internal link description for element " + _path);
        } else if (_pageElem instanceof Table)  {
            Assert.assertEquals(((Table) _pageElem).getBodyRows().size(),
                                ((Table) _compElem).getBodyRows().size(),
                                "check # rows of table " + _path);
            for (int ldx = 0; ldx < ((Table) _pageElem).getBodyRows().size(); ldx++)  {
                final TableRow pageRow = ((Table) _pageElem).getBodyRows().get(ldx);
                final TableRow compRow = ((Table) _compElem).getBodyRows().get(ldx);
                Assert.assertEquals(pageRow.getEntries().size(),
                                    compRow.getEntries().size(),
                                    "check # columns for rows of table " + _path + "-" + ldx);
                for (int mdx = 0; mdx < pageRow.getEntries().size(); mdx++)  {
                    checkParagraphs(_path + "-" + ldx + "-" + mdx,
                                         pageRow.getEntries().get(mdx),
                                         compRow.getEntries().get(mdx));
                }
            }
        } else if (_pageElem instanceof TableOfContents)  {
            Assert.assertEquals(((TableOfContents) _pageElem).getMaxDepth(),
                                ((TableOfContents) _compElem).getMaxDepth(),
                                "check deepth of table of contents for element " + _path);
        } else if (_pageElem instanceof TextString)  {
            Assert.assertEquals(((TextString) _pageElem).getText(),
                                ((TextString) _compElem).getText(),
                                "check text for element " + _path);
        } else if (!(_pageElem instanceof Divider) && !(_pageElem instanceof NewLine)) {
            throw new Error("unknown class " + _pageElem.getClass());
        }
    }
}
