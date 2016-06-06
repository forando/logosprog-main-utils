/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.gui.animation;

/**
 * Created by forando on 23.06.15.<br/>
 * Specifies basic animation events.
 */
public interface AnimationListener<T> {
    void onAnimationStart(T obj);
    void onAnimationEnd(T obj);
    void reDraw();
}
