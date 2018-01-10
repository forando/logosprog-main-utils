/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.extensionfunctions

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertEquals

object FunctionsTest: Spek({
    describe("assignToInt function"){
        on("adding not nullable with nullable args") {
            var a = 1
            val b : Int? = 2
            a += b
            it("should them add despite their different types") {
                assertEquals(3, a)
            }
        }

        on("adding not nullable with null") {
            var a = 1
            val b : Int? = null
            a += b
            it("should ignore args with null") {
                assertEquals(1, a)
            }
        }
    }
})
