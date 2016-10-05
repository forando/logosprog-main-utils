/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.files.builders;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by forando on 15.06.15.<br>
 *     Provides method to create {@link java.io.InputStream} objects
 *     from resource files that are in the same package as provided class.
 */
public abstract class ResourceInputStreamBuilder implements ObjectFromFileBuilder<InputStream, String> {
    @Override
    public InputStream build(String fileName) throws IOException {
        try {
            InputStream is = getObjectClass().getClassLoader().getResourceAsStream(fileName);
            return is;
        }catch (Exception ex){
            ex.printStackTrace();
            throw new IOException("Cannot build InputStream from " + fileName + " resource");
        }
    }

    /**
     * This method must be implemented by a Class the InputStream will be built from.<br>
     * Insert this line in the method body: <b>return this.getClass();</b>
     * @return {@link Class} object that is used in {@link #build(String)} method.
     */
    public abstract Class<?> getObjectClass();
}
