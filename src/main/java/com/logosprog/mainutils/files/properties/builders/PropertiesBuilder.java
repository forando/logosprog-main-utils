/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.files.properties.builders;

import com.logosprog.mainutils.files.builders.ObjectFromFileBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by forando on 25.06.15.<br>
 * Builds {@link Properties} object from resource file.
 */
public class PropertiesBuilder implements ObjectFromFileBuilder<Properties, InputStream> {

    private Properties properties;

    public PropertiesBuilder(InputStream is) throws IOException {
        this.properties = this.build(is);
    }

    @Override
    public Properties build(InputStream is) throws IOException {
        Properties properties = new Properties();
        properties.load(is);
        return properties;
    }


    public Properties getProperties(){
        return properties;
    }
}
