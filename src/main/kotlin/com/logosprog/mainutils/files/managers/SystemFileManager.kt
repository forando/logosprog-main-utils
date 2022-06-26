/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.files.managers

import java.io.*

/**
 * Provides basic operations with files.
 *
 * @param fileName A name to be given to a new file
 * @param rootDir The complete path to a root directory.
 * @param subDir Optional. The subdirectory to root directory.
 * @throws [IOException] if either [fileName] or [rootDir] is empty
 */
open class SystemFileManager(val fileName: String, private val rootDir: String, private val subDir: String? = null){

    init {
        if (rootDir.isEmpty() || fileName.isEmpty())
            throw IOException("You must pass in fileName and rootDir")
    }

    fun fileExists(): Boolean = File(getFilePath()).exists()

    /**
     * This method constructs whole path to desired system file.
     * This is where you can change the default location of all
     * files of your application.
     * if it's given, then it will be included in the fileName path
     * @return Constructed path to the requested file
     */
    protected fun getFilePath(): String {
        return if (null != subDir) {
            rootDir + File.separator + subDir + File.separator + fileName
        } else {
            rootDir + File.separator + fileName
        }
    }

    /**
     * Creates parent DIR if it does not exist and than creates
     * an empty file
     * @return True - if operation was successful.
     */
    fun createEmptyFile(): Boolean {
        val f = File(getFilePath())
        var dirCreated = true
        var newFileCreated = false

        try {
            if (!f.parentFile.exists())
                dirCreated = f.parentFile.mkdirs()
            newFileCreated = f.createNewFile()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return dirCreated && newFileCreated
    }

    /**
     * Deletes the predefined file.
     * @return True - if operation was successful.
     */
    fun deleteDefaultFile(): Boolean = File(getFilePath()).delete()

    /**
     * This method copies default file from internal resource folder to a specific
     * project folder.
     * @return True - if operation was successful.
     */
    fun generateDefaultFile(): Boolean {
        val path = getFilePath()
        val inputStream: InputStream = this.javaClass.classLoader.getResourceAsStream(fileName) ?: return false
        if (!createEmptyFile()){
            inputStream.close()
            return false
        }
        try {
            FileOutputStream(path).use {
                val buffer = ByteArray(1024)
                var length = 0

                while (
                        inputStream
                                .read(buffer)
                                .let {
                                    length = it
                                    it > 0
                                }
                ) {
                    it.write(buffer, 0, length)
                }
            }
            return true
        } catch (ex: Exception) {
            ex.printStackTrace()
            return false
        }finally {
            inputStream.close()
        }

        try {
            inputStream.close()
        }catch (ignored: IOException){}

        return true
    }
}

