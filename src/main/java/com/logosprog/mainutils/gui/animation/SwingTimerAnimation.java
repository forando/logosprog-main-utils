/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.gui.animation;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Created by forando on 23.06.15.<br>
 * Performs GUI com.logosprog.display.animation on a basis of {@link Timer} events.
 */
public abstract class SwingTimerAnimation<T> {
    protected Timer timer;

    /**
     * The object (normally GUI object) on which com.logosprog.display.animation is performed.
     */
    protected T mainObject;

    protected AnimationListener<T> listener;

    /**
     * <b>Attention!</b> {@link #createTimer(int, ActionListener)} must be called right
     * after this constructor invocation.
     * @param obj The object (normally GUI object) on which com.logosprog.display.animation is performed.
     */
    protected SwingTimerAnimation(T obj){
        this.mainObject = obj;
    }

    /**
     * Initialize the {@link Timer} object.
     * @param interval Interval at which the timer <b>actionPerformed</b> events will be fired up.
     * @param actionListener The listener interface for receiving <b>actionPerformed</b> events
     *                       and providing some com.logosprog.display.animation with {@link #mainObject}.
     */
    protected void createTimer(int interval, ActionListener actionListener){
        timer = new Timer(interval, actionListener);
    }

    /**
     * Starts the timer with predefined settings.
     */
    public void start(){
        if (listener != null) listener.onAnimationStart(mainObject);
        timer.start();
    }

    /**
     * Stops the timer.
     */
    public void stop(){
        if (listener != null) listener.onAnimationEnd(mainObject);
        timer.stop();
    }

    public void addListener(AnimationListener<T> listener) {
        this.listener = listener;
    }
}
