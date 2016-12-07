/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.files.properties.builders

import com.logosprog.mainutils.files.builders.TemplateFileBuilder1
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*

/**
 * Created by forando on 14.06.15.
 * This class constructs external .txt files **from default
 * template files**.
 */
abstract class TemplatePropertiesBuilder
/**

 * @param fileName A file name that will be used to construct [Properties] object from
 * *
 * @param rootDir Application root directory
 * *
 * @param subDir Optional. A subdirectory name the file will be located
 * *
 * @param is An input stream to read a file from.
 * *
 * @throws IOException If input stream is NULL
 */
@Throws(IOException::class)
constructor(fileName: String, rootDir: String, subDir: String, inputStream: InputStream?) : TemplateFileBuilder1<Properties>(fileName, rootDir, subDir) {

    init {
        if (inputStream == null) throw IOException("InputStream of an internal .txt template file is NULL!")
        this.build(inputStream)
    }

    @Throws(IOException::class)
    override fun getObjectFromExternalFile(): Properties {

        //bug: we have to do like this cause cyrillic will not work on windows
        val properties: Properties
        var stream: InputStream? = null
        var reader: InputStreamReader? = null
        try {
            stream = FileInputStream(getFilePath())
            reader = InputStreamReader(stream, "UTF-8")
            properties = Properties()
            properties.load(reader)

        } finally {
            if (stream != null) stream.close()
            if (reader != null) {
                reader.close()
            }
        }
        return properties
    }

    @Throws(IOException::class)
    override fun setMainObject() {
        /*
        At this moment these files are read only.
         */
    }

    /**
     * **Attention!!!** you must be sure that [.build] method
     * has been invoked before this one.
     * @return [Properties] object
     */
    val properties: Properties
        get() = mainObject


}
