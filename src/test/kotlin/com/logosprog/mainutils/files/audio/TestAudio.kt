/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.files.audio

import org.junit.Assert.*
import org.junit.Test

class TestAudio{
    @Test fun testAudio(){
        val audio = Audio1(AudioResourceInputStreamBuilder(), "notify.wav")
        assertNotNull(audio)
        assertNotNull(audio.Play())
    }
}
