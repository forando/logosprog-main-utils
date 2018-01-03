/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.files.managers

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

object SystemFileManagerTest: Spek({

    val testFileName = "test.txt"
    val rootDir = "C:\\Windows"
    val subDir = "System32"

    describe("SystemFileManager"){
        on("init without subDir parameter") {
            val manager = SystemFileManager("test", "versiya")
            it("should not be null") {
                assertNotNull(manager)
            }
        }
        on("init without subDir parameter for existing file") {
            val manager = SystemFileManager("system.ini", rootDir)
            val exist = manager.fileExists()
            it("should confirm that file really exists") {
                assertTrue(exist)
            }
        }
        on("init without subDir parameter for non existing file") {
            val manager = SystemFileManager("wrongFile.txt", rootDir)
            val exist = manager.fileExists()
            it("should confirm that file does not exist") {
                assertFalse(exist)
            }
        }
        on("init with subDir parameter for existing file") {
            val manager = SystemFileManager("cmd.exe", rootDir, subDir)
            val exist = manager.fileExists()
            it("should confirm that file really exists") {
                assertTrue(exist)
            }
        }
        on("init with subDir parameter for non existing file") {
            val manager = SystemFileManager(testFileName, rootDir, subDir)
            val exist = manager.fileExists()
            it("should confirm that file does not exist") {
                assertFalse(exist)
            }
        }
        context("init with a not existing Dir") {
            val manager = SystemFileManager(testFileName, "notExistingDir")
            on("creating default file") {
                val created = manager.createEmptyFile()
                it("should return false") {
                    assertFalse(created)
                }
            }
            /*on("deleting default file") {
                val deleted = manager.deleteDefaultFile()
                it("should return false") {
                    assertFalse(deleted)
                }
            }*/
            on("generating default file") {
                val generated = manager.generateDefaultFile()
                it("should return false") {
                    assertFalse(generated)
                }
            }
        }
        on("generating default file with a wrong template") {
            val manager = SystemFileManager("wrongTemplateFile.txt", rootDir)
            val generated = manager.generateDefaultFile()
            it("should return false") {
                assertFalse(generated)
            }
        }
    }
})
