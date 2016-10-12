/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.files.builders

import com.logosprog.mainutils.files.managers.SystemFileManager
import com.logosprog.mainutils.system.ConsoleMessage1
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/**
 * Constructs external system files **from default
 * template files**.
 */
abstract class TemplateFileBuilder<T>
/**

 * @param fileName A file name that will be used to construct [T] object from
 * *
 * @param rootDir Application root directory
 * *
 * @param subDir Optional. A subdirectory name the file will be located
 * *
 * @throws IOException If either **fileName** or **rootDir** is null
 */
@Throws(IOException::class)
constructor(fileName: String, rootDir: String, subDir: String) : SystemFileManager(fileName, rootDir, subDir), ObjectFromFileBuilder<T, InputStream> {

    private val TAG: String

    /**
     * The object of predefined type that we construct from external file
     */
    /**
     * Implement this method to return for user [.mainObj] **if it's
     * been already created**
     * @return [.mainObj]
     */
    protected var mainObject: T? = null
        private set


    init {
        TAG = this.javaClass.simpleName
    }

    @Throws(IOException::class)
    override fun build(element: InputStream): T {
        if (fileExists()) {
            mainObject = objectFromExternalFile
        } else {
            val created = generateDefaultFile(element)
            if (created) {
                mainObject = objectFromExternalFile
                if (listener != null) listener!!.onFileGenerated()
            } else {
                ConsoleMessage1.printErrorMessage("$TAG.build: An error occurred during $fileName file creation.")
            }
        }
        return mainObject as T
    }

    /**
     * Implement this method to construct for user a [T] object from external file
     * @return a [T] object
     * *
     * @throws IOException If [T] object construction failed.
     */
    protected abstract val objectFromExternalFile: T

    /**
     * This method must be implemented in order to save a [T]
     * object back to external file.
     * @throws IOException If saving to external file failed.
     */
    @Throws(IOException::class)
    protected abstract fun setMainObject()


    /**
     * This method copies default file from internal app folders to specific
     * project folder.
     * @param inputStream Input stream to generate the file from
     * *
     * @return True - if operation is successful.
     */
    protected fun generateDefaultFile(inputStream: InputStream): Boolean {
        val path = getFilePath()

        if (!createEmptyFile()){
            inputStream.close()
            return false
        }

        val outputStream: OutputStream? = FileOutputStream(path)
        val buffer = ByteArray(1024)
        var length: Int = 0

        while (inputStream.read(buffer).let{
            length = it
            it > 0
        }){
            outputStream?.write(buffer, 0, length)
        }

        return true
    }

    internal var listener: TemplateFileBuilderListener? = null

    fun addTemplateFileBuilderListener(listener: TemplateFileBuilderListener) {
        this.listener = listener
    }


    interface TemplateFileBuilderListener {
        /**
         * Notifies when the desired file has been just generated from the default template file.
         */
        fun onFileGenerated()
    }
}
