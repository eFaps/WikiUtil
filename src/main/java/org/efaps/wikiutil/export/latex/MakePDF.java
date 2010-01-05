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

package org.efaps.wikiutil.export.latex;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.efaps.wikiutil.parser.gwiki.javacc.ParseException;

/**
 * Converts Wiki pages to PDF.
 *
 * @author The eFaps Team
 * @version $Id$
 * TODO: parsing of one Wiki page
 */
public class MakePDF
{
    /**
     * Resource directory where the &quot;original&quot; Tex files are located.
     */
    private static final String RESOURCEDIR = "org/efaps/wikiutil/export/latex/";

    /**
     * Temporary directory where the output files are written.
     */
    private final File tempDir;

    /**
     * Defines the variables used for the Latex converter.
     */
    private final Map<String, String> variables = new HashMap<String, String>();

    /**
     * File extension for Wiki files.
     *
     * @see #setWikiFileExtension(String)
     */
    private String wikiFileExtension = ".wiki";

    /**
     * Name of the Wiki index file.
     *
     * @see #setWikiIndexName(String)
     */
    private String wikiIndexName;

    /**
     * Root URI where all Wiki pages are located.
     *
     * @see #setWikiRootURI(URI)
     */
    private URI wikiRootURI;

    /**
     *
     */
    private File wikiTargetFile;

    /**
     * Initializes the temporary directory.
     *
     * @param _tempDir  temporary directory
     */
    public MakePDF(final File _tempDir)
    {
        this.tempDir = _tempDir;
    }

    /**
     *
     * @throws IOException      if files could not be opened or written
     * @throws ParseException   if parsing of the Wiki pages failed
     * @see #convertByIndex()
     * @see #copyTexFile(String)
     * @see #writeVariablesFile()
     * @see #executePDFLatex()
     */
    public void execute()
        throws IOException, ParseException
    {
        if (this.wikiIndexName != null)  {
            this.convertByIndex();
        } else  {
            // TODO: parsing of one Wiki page
            throw new IOException("unkown Wiki index page");
        }
        this.copyTexFile("book.tex");
        this.copyTexFile("frontpage.tex");
        this.writeVariablesFile();
        if (this.executePDFLatex())  {
            this.executePDFLatex();
            if (this.wikiTargetFile != null)  {
                final Reader in = new FileReader(new File(this.tempDir, "book.pdf"));
                try {
                    final Writer out = new FileWriter(this.wikiTargetFile);
                    try  {
                        IOUtils.copy(in, out);
                    } finally  {
                        out.close();
                    }
                } finally  {
                    in.close();
                }
            }
        }
    }

    /**
     * Defines a new variable for the convert.
     *
     * @param _key      name of the variable
     * @param _value    value of the variable
     * @return this instance
     * @see #variables
     */
    public MakePDF variable(final String _key,
                            final String _value)
    {
        this.variables.put(_key, _value);
        return this;
    }

    /**
     * Defines the file extension for Wiki pages.
     *
     * @param _extension    file extension for Wiki pages
     * @return this instance
     * @see #wikiFileExtension
     */
    public MakePDF setWikiFileExtension(final String _extension)
    {
        this.wikiFileExtension = _extension;
        return this;
    }

    /**
     * Defines the {@link #wikiIndexName name} of the Wiki page with the index.
     *
     * @param _name     name of the Wiki index file
     * @return this instance
     * @see #wikiIndexName
     */
    public MakePDF setWikiIndexName(final String _name)
    {
        this.wikiIndexName = _name;
        return this;
    }

    /**
     * Defines the root URL where all Wiki files are located.
     *
     * @param _rootURI  root URI of the Wiki files
     * @return this instance
     * @see #wikiRootURI
     */
    public MakePDF setWikiRootURI(final URI _rootURI)
    {
        this.wikiRootURI = _rootURI;
        return this;
    }

    /**
     * Defines the name and path of the target file.
     *
     * @param _targetFile   target file
     * @return this instance
     * @see #wikiTargetFile
     */
    public MakePDF setWikiTargetFile(final File _targetFile)
    {
        this.wikiTargetFile = _targetFile;
        return this;
    }

    /**
     * Converts the Wiki pages by a defined Wiki index page.
     *
     * @throws IOException      if Wiki pages could not be opened or Latex
     *                          files could not be written
     * @throws ParseException   if parse of the Wiki pages failed
     */
    protected void convertByIndex()
        throws IOException, ParseException
    {
        new WikiIndex2Tex(this.tempDir,
                          this.wikiRootURI,
                          this.wikiFileExtension,
                          this.wikiIndexName)
            .convert();
    }

    /**
     * Writes the variables file.
     *
     * @throws IOException if content file could not be written
     */
    protected void writeVariablesFile()
        throws IOException
    {
        final File content = new File(this.tempDir, "variables.tex");
        Writer outp = null;
        try  {
            outp = new FileWriter(content);
            for (final Map.Entry<String, String> varEntry : this.variables.entrySet())  {
                outp.append("\\newcommand{\\").append(varEntry.getKey())
                    .append("}{").append(varEntry.getValue()).append("}\n");
            }
        } finally  {
            if (outp != null)  {
                outp.close();
            }
        }
    }

    /**
     * Executes &quot;<code>pdflatex</code>&quot; from the Latex packages and
     * converts all Latex files to related PDF file <code>book.pdf</code>.
     *
     * @return <i>true</i> if the Latex to PDF convert was successfully;
     *         otherwise <i>false</i>
     * @throws IOException if execute failed
     * @see #tempDir
     */
    protected boolean executePDFLatex()
        throws IOException
    {
        final ProcessBuilder processBuilder = new ProcessBuilder("/opt/local/bin/pdflatex", "book.tex");
        processBuilder.directory(this.tempDir);
        final Process process = processBuilder.start();

        final Reader in = new InputStreamReader(process.getInputStream());
        final Reader err = new InputStreamReader(process.getErrorStream());
    //    PrintStream out = new PrintStream(process.getOutputStream());

        Integer exitCode = null;
        for (;;) {
            if (err.ready())  {
                System.err.print((char) err.read());
            } else if (in.ready())  {
                System.out.print((char) in.read());
            } else  {
                try {
                    exitCode = process.exitValue();
                    break;
                } catch (final IllegalThreadStateException e)  {
                    try {
                        Thread.sleep(1000);
                    } catch (final InterruptedException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            }
        }
        return (exitCode != null) && (exitCode == 0);
    }

    /**
     * Copy Tex file from Java resources to the temporary directory.
     *
     * @param _name     name of the file to copy
     * @throws IOException if file could not be copied
     */
    protected void copyTexFile(final String _name)
        throws IOException
    {
        final String name = new StringBuilder(MakePDF.RESOURCEDIR).append(_name).toString();
        final InputStream in = this.getClass().getClassLoader().getResourceAsStream(name);
        if (in == null)  {
            throw new IOException("could not found " + _name);
        }
        try  {
            final OutputStream out = new FileOutputStream(new File(this.tempDir, _name));
            try  {
                IOUtils.copy(in, out);
            } finally  {
                out.close();
            }
        } finally  {
            if (in != null) {
                in.close();
            }
        }
    }
}
