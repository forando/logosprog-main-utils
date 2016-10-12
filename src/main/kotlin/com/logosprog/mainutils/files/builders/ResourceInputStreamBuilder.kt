/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.files.builders

import java.io.IOException
import java.io.InputStream

/**
 * Created by forando on 15.06.15.
 * Provides method to create [java.io.InputStream] objects
 * from resource files that are in the same package as provided class.
 */
abstract class ResourceInputStreamBuilder : ObjectFromFileBuilder<InputStream, String> {
    @Throws(IOException::class)
    override fun build(fileName: String): InputStream {
//        try {
            val inputStream = objectClass.classLoader.getResourceAsStream(fileName)
            return inputStream
        /*} catch (ex: Exception) {
            ex.printStackTrace()
            throw IOException("Cannot build InputStream from $fileName resource")
        }*/

    }

    /**
     * This method must be implemented by a Class the InputStream will be built from.
     * Insert this line in the method body: **return this.getClass();**
     * @return [Class] object that is used in [.build] method.
     */
    abstract val objectClass: Class<*>
}
