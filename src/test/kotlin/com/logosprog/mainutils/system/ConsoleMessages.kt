/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.system

import org.junit.Test
import org.junit.Assert.*

class ConsoleMessages{
    @Test fun testPrintErrorMessage(){
        assertEquals("error", printErrorMessage("error"))
    }

    @Test fun testPrintInfoMessage(){
        assertEquals("info", printInfoMessage("info"))
    }

    @Test fun testPrintDebugMessage(){
        assertEquals("debug", printDebugMessage("debug"))
    }

    @Test fun testPrintDebugMessageWithFalseCondition(){
        val message = ConditioningConsoleMessage(false)
        assertEquals(null, message.printDebugMessage("debug"))
    }

    @Test fun testPrintDebugMessageWithTrueCondition(){
        val message = ConditioningConsoleMessage(true)
        assertEquals("debug", message.printDebugMessage("debug"))
    }
}
