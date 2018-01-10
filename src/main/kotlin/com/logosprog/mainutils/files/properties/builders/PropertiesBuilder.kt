/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE", "unused")

package com.logosprog.mainutils.files.properties.builders

import com.logosprog.mainutils.files.builders.ObjectFromFileBuilder
import java.io.IOException
import java.io.InputStream
import java.util.*

/**
 * Builds [Properties] object from resource file.
 *
 * Created by forando on 25.06.15.
 */
open class PropertiesBuilder @Throws(IOException::class)
constructor(inputStream: InputStream) : ObjectFromFileBuilder<Properties, InputStream> {

    private val properties: Properties

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
