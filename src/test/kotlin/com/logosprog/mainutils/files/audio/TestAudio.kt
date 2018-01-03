/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.files.audio

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertNotNull

object AudioTest: Spek({
    describe("Audio class"){
        val audio = Audio(AudioResourceInputStreamBuilder(), "notify.wav")
        on("creating new instance") {
            it("should not be null") {
                assertNotNull(audio)
            }
        }
        on("calling Play") {
            val sourceDataLine = audio.Play()
            it("should return SourceDataLine") {
                assertNotNull(sourceDataLine)
            }
        }
    }
})
