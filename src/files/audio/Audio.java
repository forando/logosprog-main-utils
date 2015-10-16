/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package files.audio;

import files.ResourceInputStreamBuilder;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by forando on 15.06.15.<br>
 *     This Class operates audio stream (Play, Stop etc.)
 */
public class Audio {

    AudioFormat audioFormat;
    AudioInputStream audioInputStream;
    SourceDataLine sourceDataLine;
    DataLine.Info dataLineInfo;
    boolean stopPlayback = false;
    boolean playbackFinished = true;
    ResourceInputStreamBuilder streamBuilder;
    String audioFileName;

    public Audio(ResourceInputStreamBuilder streamBuilder, String fileName) throws IOException {
        streamBuilder.build(fileName);
        this.streamBuilder = streamBuilder;
        this.audioFileName = fileName;
    }

    public void Play() {
        if (playbackFinished) {

            try {
                this.audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(streamBuilder.build(audioFileName)));
                this.audioFormat = audioInputStream.getFormat();
                System.out.println(audioFormat);
                this.dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
                this.sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
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
                new PlayBackThread().start();
            } catch (IOException |UnsupportedAudioFileException | LineUnavailableException e) {
                e.printStackTrace();
            }

        }
    }

    public void Stop() {
        stopPlayback = true;
    }

    public void Reset() {
        stopPlayback = false;
    }

    /**
     * Class to Play back the variables from the notificationSound file.
     */
    class PlayBackThread extends Thread {

        public static final String THREAD_NAME = "PlayBackThread";

        byte tempBuffer[] = new byte[10000];
        int readFromInputStream;

        public PlayBackThread(){
            this.setName(THREAD_NAME);
        }

        @Override
        public void run() {
            playbackFinished = false;
            try {
                sourceDataLine.open(audioFormat);
                sourceDataLine.start();
                /*
                Keep looping until the input read method returns -1 for empty
                stream or the user clicks the Stop button causing stopPlayback
                to switch from false to true.
                 */
                while ((readFromInputStream = audioInputStream.read(
                        tempBuffer, 0, tempBuffer.length)) != -1
                        && !stopPlayback) {
                    if (readFromInputStream > 0) {
                        /*
                        Write variables to the internal buffer of the
                        variables line where it will be delivered to the speaker.
                         */
                        sourceDataLine.write(
                                tempBuffer, 0, readFromInputStream);
                    }
                }
                /*
                Block and wait for internal buffer
                 of the variables line to empty.
                 */
                sourceDataLine.drain();
                sourceDataLine.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            playbackFinished = true;

            //Prepare to playback another file
            // stopBtn.setEnabled(false);
            //playBtn.setEnabled(true);
            stopPlayback = false;
        }
    }
}
