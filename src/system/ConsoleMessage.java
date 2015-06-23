/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package system;

/**
 * Created by forando on 14.06.15.<br>
 *     Provides different static methods to print messages to console
 */
public class ConsoleMessage {
    /**
     * ANSI escape code to reset everything that goes
     * after it to defaults
     */
    public static final  String ANSI_RESET = "\u001B[0m";
    /**
     * ANSI escape code for black color
     */
    public static final String ANSI_BLACK = "\u001B[30m";
    /**
     * ANSI escape code for red color
     */
    public static final String ANSI_RED = "\u001B[31m";
    /**
     * ANSI escape code for green color
     */
    public static final String ANSI_GREEN = "\u001B[32m";
    /**
     * ANSI escape code for yellow color
     */
    public static final String ANSI_YELLOW = "\u001B[33m";
    /**
     * ANSI escape code for blue color
     */
    public static final String ANSI_BLUE = "\u001B[34m";
    /**
     * ANSI escape code for purple color
     */
    public static final String ANSI_PURPLE = "\u001B[35m";
    /**
     * ANSI escape code for cyan color
     */
    public static final String ANSI_CYAN = "\u001B[36m";
    /**
     * ANSI escape code for white color
     */
    public static final String ANSI_WHITE = "\u001B[37m";

    /**
     * Prints the message to console in <b>RED</b> color.
     * @param message A message to be printed to a console.
     */
    public static void printErrorMessage(String message){
        System.err.println(message);
    }

    /**
     * This method wraps an input message with ANSI escape code
     * that allows to print that message to console in <b>BLUE</b> color.<br>
     * This formatting doesn't work on Windows console.
     * @param message A message to be printed to a console.
     */
    public static void printInfoMessage(String message){
        System.out.println(ANSI_BLUE + message + ANSI_RESET);
    }
}
