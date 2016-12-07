/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.files.properties.builders

import com.logosprog.mainutils.files.builders.ObjectFromFileBuilder1
import java.io.IOException
import java.io.InputStream
import java.util.*

/**
 * Created by forando on 25.06.15.
 * Builds [Properties] object from resource file.
 */
open class PropertiesBuilder @Throws(IOException::class)
constructor(inputStream: InputStream) : ObjectFromFileBuilder1<Properties, InputStream> {

    val properties: Properties

    init {
        this.properties = this.build(inputStream)
    }

    @Throws(IOException::class)
    override fun build(inputStream: InputStream): Properties {
        val properties = Properties()
        properties.load(inputStream)
        return properties
    }
}
