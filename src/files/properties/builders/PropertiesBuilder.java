/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package files.properties.builders;

import files.ObjectFromFileBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by forando on 25.06.15.<br/>
 * Builds {@link Properties} object from resource file.
 */
public class PropertiesBuilder implements ObjectFromFileBuilder<Properties, InputStream> {
    @Override
    public Properties build(InputStream is) throws IOException {
        Properties properties = new Properties();
        properties.load(is);
        return properties;
    }
}