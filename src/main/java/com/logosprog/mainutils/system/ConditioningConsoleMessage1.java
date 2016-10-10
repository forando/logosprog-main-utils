/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.system;

/**
 * Created by forando on 25.06.15.<br>
 * This class adds some conditions to {@link ConsoleMessage} class functionality.
 */
public class ConditioningConsoleMessage1 {

    /**
     * Permission flag to print messages that are only needed for debugging.
     */
    private boolean printDebugMessages;

    public ConditioningConsoleMessage1(boolean printDebugMessages){
        this.printDebugMessages = printDebugMessages;
    }

    /**
     * Prints debug message only if {@link #printDebugMessages} = TRUE.<br><br>
     * This method wraps an input message with ANSI escape code
     * that allows to print that message to console in <b>GREEN</b> color.<br>
     * This formatting doesn't work on Windows console.
     * @param message The message to be printed to a console.
     */
    public void printDebugMessage(String message){
        if (printDebugMessages)ConsoleMessage.printDebugMessage(message);
    }
}
