/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.files.audio

import com.logosprog.mainutils.files.builders.ResourceInputStreamBuilder
import java.io.BufferedInputStream
import javax.sound.sampled.*

/**
 * This Class operates audio stream (Play, Stop etc.)
 */
class Audio(val streamBuilder: ResourceInputStreamBuilder, val audioFileName: String){
    init {
        streamBuilder.build(audioFileName)

    }

    internal var audioFormat: AudioFormat? = null
    internal var audioInputStream: AudioInputStream? = null
    internal var sourceDataLine: SourceDataLine? = null
    internal var dataLineInfo: DataLine.Info? = null
    internal var stopPlayback = false
    internal var playbackFinished = true

    fun Play(): SourceDataLine?{
        if (playbackFinished) {
            this.audioInputStream = AudioSystem.getAudioInputStream(
                    BufferedInputStream(streamBuilder.build(audioFileName)))
            this.audioFormat = audioInputStream?.format
            println(audioFormat)
            this.dataLineInfo = DataLine.Info(SourceDataLine::class.java, audioFormat)
            this.sourceDataLine = AudioSystem.getLine(dataLineInfo) as SourceDataLine
            /*
             Create a thread to Play back the variables
             and start it running.  It will run until
             the end of file, or the Stop button is
             clicked, whichever occurs first.
             Because of the variables buffers involved,
             there will normally be a delay between
             the click on the Stop button and the
             actual termination of playback.
             */
            PlayBackThread().start()
            return this.sourceDataLine
        }
        return null
    }

    fun Stop() {
        stopPlayback = true
    }

    fun Reset() {
        stopPlayback = false
    }

    /**
     * Class to Play back the variables from the notificationSound file.
     */
    internal inner class PlayBackThread : Thread(){
        val THREAD_NAME = "PlayBackThread"

        var tempBuffer = ByteArray(10000)
        var readFromInputStream: Int? = 0

        init {
            this.name = THREAD_NAME
        }

        override fun run() {
            playbackFinished = false
            try {
                sourceDataLine?.open(audioFormat)
                sourceDataLine?.start()
                /*
                Keep looping until the input read method returns -1 for empty
                stream or the user clicks the Stop button causing stopPlayback
                to switch from false to true.
                 */
                while (!stopPlayback) {
                    readFromInputStream = audioInputStream?.read(tempBuffer, 0, tempBuffer.size)
                    if (readFromInputStream == -1)
                        break
                    if (readFromInputStream!! > 0) {
                        /*
                        Write variables to the internal buffer of the
                        variables line where it will be delivered to the speaker.
                         */
                        sourceDataLine?.write(tempBuffer, 0, readFromInputStream as Int)
                    }
                }
                /*
                Block and wait for internal buffer
                 of the variables line to empty.
                 */
                sourceDataLine?.drain()
                sourceDataLine?.close()

            } catch (e: Exception) {
                e.printStackTrace()
            }


            playbackFinished = true

            //Prepare to playback another file
            // stopBtn.setEnabled(false);
            //playBtn.setEnabled(true);
            stopPlayback = false
        }
    }
}
