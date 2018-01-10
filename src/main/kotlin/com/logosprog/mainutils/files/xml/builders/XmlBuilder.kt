package com.logosprog.mainutils.files.xml.builders

import com.logosprog.mainutils.files.managers.SystemFileManager
import org.w3c.dom.Document
import java.io.File
import java.io.InputStream
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

/**
 * Loads .xml into a memory and converts it into [Document]
 */
open class XmlBuilder(fileName: String, rootDir: String, subDir: String): SystemFileManager(fileName, rootDir, subDir){

    private var inputStream: InputStream? = null

    constructor(inputStream: InputStream?):this("any.txt", "anyDir", ""){
        this.inputStream = inputStream
    }

    init {
        if (fileExists())
            this.inputStream = File(getFilePath()).inputStream()
    }

    val xmlDocument: Document?
        get() {
            val f = DocumentBuilderFactory.newInstance()
            f.isValidating = false
            val builder: DocumentBuilder? = f.newDocumentBuilder()
            return if (inputStream != null)
                builder?.parse(inputStream)
            else
                null
        }
}
