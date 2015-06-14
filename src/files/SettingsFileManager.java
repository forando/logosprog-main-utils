/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package files;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by forando on 14.06.15.<br>
 * This class provides basic operations with *.txt files that are
 * used by each module (application) as settings/properties files
 */
public class SettingsFileManager extends SystemFileManager {
    /**
     * The folder name of where all *.txt config files are to be
     * located by default
     */
    public static final String DIR_NAME = "settings";

    public static boolean fileExists(String fileName){

        return SettingsFileManager.fileExists(fileName, DIR_NAME);
    }

    /**
     * This method returns desired property object according to given file name
     * @param fileName *.properties file name without relevant path
     * @return particular {@link Properties} object
     * @throws IOException
     */
    public static Properties getPropertiesObject(String fileName) throws IOException {
        Properties properties;
        if (SettingsFileManager.fileExists(fileName)){
            properties = new Properties();
            properties.load(new FileInputStream(SettingsFileManager.getFilePath(fileName, DIR_NAME)));
        }else{
            throw new FileNotFoundException(SettingsFileManager.getFilePath(fileName, DIR_NAME));
        }

        return properties;
    }

    /**
     * This method copies default *.txt file from internal app folders to specific
     * project setting folder.
     * @param fileName A name to be given to a new file.
     * @param rootDir The complete path to root directory.
     * @return True - if operation is successful.
     */
    public static boolean generateDefaultFile(String fileName, String rootDir){

        return files.SystemFileManager.generateDefaultFile(fileName, rootDir, DIR_NAME);
    }
}
