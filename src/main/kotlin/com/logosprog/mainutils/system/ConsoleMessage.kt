/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

@file:Suppress("PropertyName", "unused")

package com.logosprog.mainutils.system

/**
 * Prints the message to console in **RED** color.
 * @param message A message to be printed to a console.
 */
fun printErrorMessage(message: String):String {
    System.err.println(message)
    return message
}

/**
 * This method wraps an input message with ANSI escape code
 * that allows to print that message to console in **BLUE** color.
 * This formatting doesn't work on Windows console.
 * @param message The message to be printed to a console.
 */
fun printInfoMessage(message: String):String {
    println(ANSI_BLUE + message + ANSI_RESET)
    return message
}

/**
 * This method wraps an input message with ANSI escape code
 * that allows to print that message to console in **GREEN** color.
 * This formatting doesn't work on Windows console.
 * @param message The message to be printed to a console.
 */
fun printDebugMessage(message: String):String {
    println(ANSI_GREEN + message + ANSI_RESET)
    return message
}

/**
 * ANSI escape code to reset everything that goes
 * after it to defaults
 */
val ANSI_RESET = "\u001B[0m"
/**
 * ANSI escape code for black color
 */
val ANSI_BLACK = "\u001B[30m"
/**
 * ANSI escape code for red color
 */
val ANSI_RED = "\u001B[31m"
/**
 * ANSI escape code for green color
 */
val ANSI_GREEN = "\u001B[32m"
/**
 * ANSI escape code for yellow color
 */
val ANSI_YELLOW = "\u001B[33m"
/**
 * ANSI escape code for blue color
 */
val ANSI_BLUE = "\u001B[34m"
/**
 * ANSI escape code for purple color
 */
val ANSI_PURPLE = "\u001B[35m"
/**
 * ANSI escape code for cyan color
 */
val ANSI_CYAN = "\u001B[36m"
/**
 * ANSI escape code for white color
 */
val ANSI_WHITE = "\u001B[37m"
