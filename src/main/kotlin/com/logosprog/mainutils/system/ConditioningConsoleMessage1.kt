/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.system

/**
 * This class adds some conditions to {@link ConsoleMessage} class functionality.
 * @param printDebugMessages Permission flag to print messages that are only needed for debugging.
 */
class ConditioningConsoleMessage1(private val printDebugMessages: Boolean){

    /**
     * Prints debug message only if [.printDebugMessages] = TRUE.
     * This method wraps an input message with ANSI escape code
     * that allows to print that message to console in **GREEN** color.
     * This formatting doesn't work on Windows console.
     * @param message The message to be printed to a console.
     */
    fun printDebugMsg(message: String): String? {
        return if (printDebugMessages) {
            printDebugMessage(message)
            message
        }else
            null
    }
}
