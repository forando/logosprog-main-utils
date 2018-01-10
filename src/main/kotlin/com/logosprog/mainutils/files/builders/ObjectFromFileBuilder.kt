/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.files.builders

import java.io.IOException

/**
 * Created by forando on 14.06.15.
 * This interface has to be implemented in order
 * to construct an object from external file.
 */
interface ObjectFromFileBuilder<out T, in E> {
    /**
     * Factory method that constructs object of predefined type
     * from external file.
     * @param element Any element that may be needed to construct a [T] object.
     *                If no element needed, NULL can be passed.
     *
     * @return An object of desired type
     * @throws IOException If the element cannot be accessed.
     */
    @Throws(IOException::class)
    fun build(element: E): T
}
