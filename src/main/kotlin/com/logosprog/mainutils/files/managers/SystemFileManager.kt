/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.files.managers

import java.io.*

/**
 * Provides basic operations with files that are
 * used by each module/application. That's why they are called
 * system files.
 * @param fileName A name to be given to a new file
 * @param rootDir The complete path to a root directory.
 * @param subDir Optional. The subdirectory to root directory.
 * if it's given, then it will be included in the fileName path
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
        /*
        Checking if directory for files already exists
         */
        if (!f.parentFile.exists())
            dirCreated = f.parentFile.mkdirs()

        return dirCreated && f.createNewFile()
    }

    /**
     * Deletes the predefined file.
     * @return True - if operation was successful.
     */
    fun deleteDefaultFile(): Boolean = File(getFilePath()).delete()

    /**
     * This method copies default file from internal resource folder to specific
     * project folder.
     * @return True - if operation was successful.
     */
    fun generateDefaultFile(): Boolean {
        val path = getFilePath()
        val inputStream: InputStream = SystemFileManager::class.java.classLoader.getResourceAsStream(fileName) ?:
                return false
        if (!createEmptyFile()){
            inputStream.close()
            return false
        }
        val outputStream: OutputStream? = FileOutputStream(path)
        val buffer = ByteArray(1024)
        var length = 0

        while (inputStream.read(buffer).let{
            length = it
            it > 0
        }){
            outputStream?.write(buffer, 0, length)
        }

        return true
    }
}

