/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package files.properties;

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

    Properties properties =null;

    public SettingsFileManager(String fileName, String rootDir, String subDir) throws IOException {
        super(fileName, rootDir, subDir);
    }

    public Properties getPropertiesObject() throws IOException {
        if (fileExists()) {
            properties = new Properties();
            properties.load(new FileInputStream(getFilePath()));
        }else{
            throw new FileNotFoundException(getFilePath());
        }

        return properties;
    }

    public String getFileName(){
        return fileName;
    }
}
