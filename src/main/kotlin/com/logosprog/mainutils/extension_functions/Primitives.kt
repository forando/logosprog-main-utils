/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.extension_functions


operator fun Int.plus(b: Int?): Int {
    var a = this
    if (b != null) {
        a += b
    }
    return a
}
