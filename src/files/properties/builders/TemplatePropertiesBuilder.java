/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package files.properties.builders;

import files.TemplateFileBuilder;
import org.w3c.dom.Document;
import system.ConsoleMessage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by forando on 14.06.15.<br>
 * This class constructs external .txt files <b>from default
 * template files</b>.
 */
public abstract class TemplatePropertiesBuilder extends TemplateFileBuilder<Properties> {
    /**
     * @param fileName A file name that will be used to construct {@link Properties} object from
     * @param rootDir  Application root directory
     * @param subDir   Optional. A subdirectory name the file will be located
     * @throws IOException If either <b>fileName</b> or <b>rootDir</b> or <b>is</b> is null
     */
    public TemplatePropertiesBuilder(String fileName, String rootDir, String subDir, InputStream is) throws IOException {
        super(fileName, rootDir, subDir);
        if (is == null) throw new IOException("InputStreame of an internal .txt template file is NULL!");
        this.build(is);
    }

    @Override
    protected Properties getObjectFromExternalFile() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(getFilePath()));
        return properties;
    }

    @Override
    protected void setMainObject() throws IOException {
        //next: implement it later
    }

    /**
     * <b>Attention!!!</b> you must be sure that {@link #build(InputStream)} method
     * has been invoked before this one.
     * @return {@link Properties} object
     */
    public Properties getProperties(){
        return getMainObject();
    }


}
