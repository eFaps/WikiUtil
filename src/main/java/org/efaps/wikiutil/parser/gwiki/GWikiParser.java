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

import java.io.InputStream;
import java.io.Reader;

import org.efaps.wikiutil.page.WikiPage;
import org.efaps.wikiutil.parser.IWikiEventModel;
import org.efaps.wikiutil.parser.WEM2WOM;
import org.efaps.wikiutil.parser.gwiki.javacc.ParseException;
import org.efaps.wikiutil.parser.gwiki.javacc.WikiParser;

/**
 * Google Wiki parser utility class to parse Wiki stream in Google Code style.
 *
 * @author The eFaps Team
 * @version $Id$
 */
public final class GWikiParser
{
    /**
     * Constructor defined so that this utility class could not be initialized.
     */
    private GWikiParser()
    {
    }

    /**
     * Parses given Wiki in Google code style defined with input stream
     * <code>_in</code> in <code>_encoding</code> into the Wiki object model.
     *
     * @param _in       input stream with the Wiki code in Google style
     * @param _encoding encoding of the input stream
     * @return parsed Wiki page from the Wiki object model
     * @throws ParseException if the Wiki page could not be parsed
     */
    public static WikiPage parse(final InputStream _in,
                                 final String _encoding)
        throws ParseException
    {
        final WEM2WOM wem2wom = new WEM2WOM();
        GWikiParser.parse(wem2wom, _in, _encoding);
        return wem2wom.getPage();
    }

    /**
     * Parses given Wiki in Google code style defined with <code>_reader</code>
     * into the Wiki object model.
     *
     * @param _reader   reader with the Wiki code in Google style
     * @return parsed Wiki page from the Wiki object model
     * @throws ParseException if the Wiki page could not be parsed
     */
    public static WikiPage parse(final Reader _reader)
        throws ParseException
    {
        final WEM2WOM wem2wom = new WEM2WOM();
        GWikiParser.parse(wem2wom, _reader);
        return wem2wom.getPage();
    }

    /**
     * Parses given Wiki in Google code style defined with input stream
     * <code>_in</code> in <code>_encoding</code> into the Wiki event model.
     *
     * @param _wem      Wiki event model to which is parsed
     * @param _in       input stream with the Wiki code in Google style
     * @param _encoding encoding of the input stream
     * @throws ParseException if the Wiki page could not be parsed
     */
    public static void parse(final IWikiEventModel _wem,
                      final InputStream _in,
                      final String _encoding)
        throws ParseException
    {
        new WikiParser(_in, _encoding).parse(new GWikiVisitor(_wem));
    }

    /**
     * Parses given Wiki in Google code style defined with <code>_reader</code>
     * into the Wiki event model.
     *
     * @param _wem      Wiki event model to which is parsed
     * @param _reader   reader with the Wiki code in Google style
     * @throws ParseException if the Wiki page could not be parsed
     */
    public static void parse(final IWikiEventModel _wem,
                             final Reader _reader)
        throws ParseException
    {
        new WikiParser(_reader).parse(new GWikiVisitor(_wem));
    }
}
