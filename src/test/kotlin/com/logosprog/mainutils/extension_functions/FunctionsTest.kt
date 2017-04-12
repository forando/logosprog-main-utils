/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.extension_functions

import org.junit.Assert.*
import org.junit.Test

class FunctionsTest{
    @Test fun assignToInt(){
        var a : Int = 1
        val b : Int? = 2
        a += b
        assertEquals(3, a)
    }

    @Test fun assignToNull(){
        var a : Int = 1
        val b : Int? = null
        a += b
        assertEquals(1, a)
    }
}
