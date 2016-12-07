/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.files.audio

import com.logosprog.mainutils.files.builders.ResourceInputStreamBuilder

/**
 * Created by forando on 15.06.15.
 * Provides method to create [java.io.InputStream] objects
 * from resource files that are in the same package as this class.
 */
class AudioResourceInputStreamBuilder : ResourceInputStreamBuilder() {


    override val objectClass: Class<*>
        get() = this.javaClass
}
