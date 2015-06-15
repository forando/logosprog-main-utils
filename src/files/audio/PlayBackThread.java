/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package files.audio;

/**
 * Created by forando on 15.06.15.
 */
public class PlayBackThread extends Thread {

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
