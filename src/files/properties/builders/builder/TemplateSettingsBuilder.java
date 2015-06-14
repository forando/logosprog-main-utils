/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package files.properties.builders.builder;

import system.ConsoleMessage;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by forando on 14.06.15.<br>
 * This class constructs external config.txt files for each application
 * settings of this Project <b>from default template files</b>
 */
public abstract class TemplateSettingsBuilder implements PropertiesBuilder {
    protected Properties properties = null;

    protected SettingsFileManager manager;

    /**
     * The name of a default config.txt file to be used for external
     * settings file creation.<br>
     *     <b>Attention!</b> this name must be exactly the same as the
     *     name of the default file in <b>Resources</b> module of this
     *     Project <b>--> folder: 'resources'</b>.
     */
    //private String fileName;

    public TemplateSettingsBuilder(String fileName, String rootDir, String subDir) throws IOException {
        //this.fileName = fileName;
        manager = new SettingsFileManager(fileName, rootDir, subDir);
    }


    @Override
    public Properties build() throws IOException {

        if (manager.fileExists()) {
            properties = manager.getPropertiesObject();
        }else{
            boolean created;
            created = manager.generateDefaultFile();
            if (created){
                properties = manager.getPropertiesObject();
            }else {
                ConsoleMessage.printErrorMessage("An error occurred during " + manager.getFileName() + " file creation.");
            }
        }
        return properties;
    }

    /**
     * This method must be call only after {@link #build}
     * @return
     */
    protected Properties getProperties(){
        return properties;
    }

    protected SettingsFileManager getManager(){
        return manager;
    }

}
