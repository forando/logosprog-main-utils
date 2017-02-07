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
class XmlBuilder(fileName: String, rootDir: String, subDir: String): SystemFileManager(fileName, rootDir, subDir){

    private var _inputStream: InputStream? = null
    private val inputStream: InputStream?

    constructor(inputStream: InputStream):this("", "", ""){

        this._inputStream = inputStream
    }

    init {
        if (fileExists()){
            this.inputStream = File(getFilePath()).inputStream()
        }else{
            this.inputStream = this._inputStream ?: null
        }
    }

    val xmlDocument: Document?
        get() {
            val f = DocumentBuilderFactory.newInstance()
            f.isValidating = false
            val builder: DocumentBuilder? = f.newDocumentBuilder()
            if (inputStream != null)
                return builder?.parse(inputStream)
            else
                return null
        }
}
