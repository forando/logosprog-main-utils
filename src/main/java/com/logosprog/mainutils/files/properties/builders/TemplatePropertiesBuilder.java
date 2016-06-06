/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.files.properties.builders;

import com.logosprog.mainutils.files.builders.TemplateFileBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
        if (is == null) throw new IOException("InputStream of an internal .txt template file is NULL!");
        this.build(is);
    }

    @Override
    protected Properties getObjectFromExternalFile() throws IOException {

        //bug: we have to do like this cause cyrillic will not work on windows
        Properties properties;
        InputStream stream = null;
        InputStreamReader reader = null;
        try {
            stream = new FileInputStream(getFilePath());
            reader = new InputStreamReader(stream,"UTF-8");
            properties = new Properties();
            properties.load(reader);

        } finally {
            if (stream != null) stream.close();
            if (reader != null) {
                reader.close();
            }
        }
        return properties;
    }

    @Override
    protected void setMainObject() throws IOException {
        /*
        At this moment these files are read only.
         */
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
