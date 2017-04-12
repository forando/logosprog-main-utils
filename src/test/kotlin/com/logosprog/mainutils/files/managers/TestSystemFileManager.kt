/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.files.managers

import org.junit.Assert.*
import org.junit.Test

class TestSystemFileManager{

    val testFileName = "test.txt"
    val rootDir = "C:\\Windows"
    val subDir = "System32"

    @Test fun testSystemFileManagerInit(){
        assertNotNull("Must be initialized with no third param passed", SystemFileManager("test", "versiya"))
    }

    @Test fun testFileExist(){
        assertTrue(SystemFileManager("system.ini", rootDir).fileExists())
        assertFalse(SystemFileManager("wrongFile.txt", rootDir).fileExists())
    }

    @Test fun testFileExistWithSubDir(){
        assertTrue(SystemFileManager("cmd.exe", rootDir, subDir).fileExists())
        assertFalse(SystemFileManager(testFileName, rootDir, subDir).fileExists())
    }

    @Test fun testCreateDeleteFile(){
        val systemFileManager = SystemFileManager("someFile.txt", rootDir)
        assertTrue(systemFileManager.createEmptyFile())
        assertTrue(systemFileManager.deleteDefaultFile())
    }

    @Test fun testGenerateDefaultFile(){
        val systemFileManager = SystemFileManager(testFileName, rootDir)
        systemFileManager.deleteDefaultFile()
        assertTrue(systemFileManager.generateDefaultFile())
    }

    @Test fun testCreateDeleteFileInNoTExistingDir(){
        val systemFileManager = SystemFileManager(testFileName, "notExistingDir")
        assertFalse(systemFileManager.createEmptyFile())
    }

    @Test fun testGenerateDefaultFileWithWrongTemplateFile(){
        val systemFileManager = SystemFileManager("wrongTemplateFile.txt", rootDir)
        assertFalse(systemFileManager.generateDefaultFile())
    }

    @Test fun testGenerateDefaultFileWithWrongRootDir(){
        val systemFileManager = SystemFileManager(testFileName, "notExistingDir")
        assertFalse(systemFileManager.generateDefaultFile())
    }
}
