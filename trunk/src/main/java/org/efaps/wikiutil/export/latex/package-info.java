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

/**
 * Converts one Wiki page or a set of Wiki pages to a PDF file.
 *
 * <b>Example:<br/></b>
 * <pre>
 * final File tempDir = new File("/tmp/");
 * new MakePDF(tempDir)
 *     .setWikiFileExtension(".wiki")
 *     .setWikiIndexName("Index")
 *     .setWikiRootURI(new File("/wiki").toURI())
 *     .variable("WikiAuthor",         "The eFaps Team")
 *     .variable("WikiVersion",        "0.7.2")
 *     .variable("WikiLicense",        "Apache License, Version 2.0")
 *     .variable("WikiLicenseFile",    "../LICENSE.txt")
 *     .variable("WikiTitle",          "eFaps Framework")
 *     .variable("WikiSubTitle",       "Manual and Overview")
 *     .variable("WikiPDFCreator",     "eFaps Wiki Util")
 *     .variable("WikiPDFKeywords",    "eFaps,Manual,Overview")
 *     .execute();
 * </pre>
 */
package org.efaps.wikiutil.export.latex;
