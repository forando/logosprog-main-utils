/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package system;

/**
 * Created by forando on 14.06.15.
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
     * This method wraps an input message with ANSI escape code
     * that allows to print that message to console in red color.<br>
     * This formatting doesn't work on Windows console.
     * @param message A message to be printed to console.
     */
    public static void printErrorMessage(String message){
        System.out.println(ANSI_RED + message + ANSI_RESET);
    }
}
