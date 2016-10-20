/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.extension_functions

operator fun Int.plusAssign(b: Int?) {
    if (b != null){
        this + b
    }
}
