/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package files.properties.builders.builder;

import files.SystemFileManager;

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


    public SettingsFileManager(String fileName, String rootDir, String subDir) throws IOException {
        super(fileName, rootDir, subDir);
    }

    /*public static boolean fileExists(String fileName){

        return fileExists(fileName, DIR_NAME);
    }*/

    /**
     * This method returns desired property object according to given file name
     * @return particular {@link Properties} object
     * @throws IOException
     */
    public Properties getPropertiesObject() throws IOException {
        Properties properties;
        if (fileExists()) {
            properties = new Properties();
            properties.load(new FileInputStream(getFilePath()));
        }else{
            throw new FileNotFoundException(getFilePath());
        }

        return properties;
    }
/*
    *//**
     * This method copies default *.txt file from internal app folders to specific
     * project setting folder.
     * @param fileName A name to be given to a new file.
     * @param rootDir The complete path to root directory.
     * @return True - if operation is successful.
     *//*
    public static boolean generateDefaultFile(String fileName, String rootDir){

        return generateDefaultFile(fileName, rootDir, DIR_NAME);
    }*/

    protected String getFileName(){
        return fileName;
    }
}
