/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.system

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertEquals
import kotlin.test.assertNull

object ConsoleMessagesTest: Spek({
    describe("ConsoleMessages class"){
        on("printError") {
            val value = printErrorMessage("test")
            it("should return an argument value") {
                assertEquals("test", value)
            }
        }
        on("printInfo") {
            val value = printInfoMessage("test")
            it("should return an argument value") {
                assertEquals("test", value)
            }
        }
        on("printDebug") {
            val value = printDebugMessage("test")
            it("should return an argument value") {
                assertEquals("test", value)
            }
        }
    }
    describe("ConditioningConsoleMessage"){
        given("An Instance with printDebugMessages = true") {
            val consoleMessage = ConditioningConsoleMessage(true)
            on("printDebug") {
                val value = consoleMessage.printDebugMsg("test")
                it("should return an argument value") {
                    assertEquals("test", value)
                }
            }
        }
        given("An Instance with printDebugMessages = false") {
            val consoleMessage = ConditioningConsoleMessage(false)
            on("printDebug") {
                val value = consoleMessage.printDebugMsg("test")
                it("should return null") {
                    assertNull(value)
                }
            }
        }
    }
})
