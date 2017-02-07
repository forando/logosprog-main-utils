/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.files.xml.builders

import com.logosprog.mainutils.files.builders.TemplateFileBuilder
import org.w3c.dom.Document
import java.io.File
import java.io.IOException
import java.io.InputStream
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

/**
 * Constructs external data.xml files **from default
 * template.xml files**.
 */
abstract class TemplateXMLBuilder
/**
 * @param fileName A file name that will be used to construct [Document] object from
 * @param rootDir Application root directory
 * @param subDir Optional. A subdirectory name the file will be located
 * @param inputStream A [InputStream] object of an internal template .xml file that will be
 *            used to generate default file from. Can be NULL if **build = false**
 * @param build indicates if [.build] method has to be invoked in the constructor.
 * @throws IOException If either **fileName** or **rootDir** or **is** is null
 */
@Throws(IOException::class)
@JvmOverloads constructor(fileName: String, rootDir: String, subDir: String, inputStream: InputStream?,
                          build: Boolean = true) : TemplateFileBuilder<Document>(fileName, rootDir, subDir) {

    init {
        if (build) {
            if (inputStream == null) throw IOException("InputStream of an internal .xml template file is NULL!")
            this.build(inputStream)
        }
    }

    override val objectFromExternalFile: Document?
        get() {
            val f = DocumentBuilderFactory.newInstance()
            f.isValidating = false
            val builder: DocumentBuilder? = f.newDocumentBuilder()
            return builder?.parse(File(getFilePath()))
        }

    @Throws(IOException::class)
    override fun setMainObject() {
        val document = mainObject
        /*
        optional, but recommended.
        read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
         */
        document?.normalize()

        try {
            val transformerFactory = TransformerFactory.newInstance()
            //transformerFactory.setAttribute("indent-number", 2);
            val transformer = transformerFactory.newTransformer()
            transformer.setOutputProperty(OutputKeys.INDENT, "yes")
            val source = DOMSource(document)
            val result = StreamResult(File(getFilePath()))
            transformer.transform(source, result)
        } catch (e: TransformerException) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    fun saveXMLDocument() {
        setMainObject()
    }

    /**
     * **Attention!!!** you must be sure that [.build] method
     * has been invoked before this one.
     * @return [Document] object
     */
    /*
    optional, but recommended.
    read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
     */
    val xmlDocument: Document?
        get() {
            val document = mainObject
            document?.documentElement?.normalize()
            return document
        }
}
