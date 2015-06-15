/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package files.audio;

import javax.sound.sampled.*;
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

    public Audio(InputStream is) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        this.audioInputStream = AudioSystem.getAudioInputStream(is);
        this.audioFormat = audioInputStream.getFormat();
        System.out.println(audioFormat);
        this.dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
        this.sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
    }

    public void play(){
        if (playbackFinished) {

        }
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
            super.run();
        }
    }
}
