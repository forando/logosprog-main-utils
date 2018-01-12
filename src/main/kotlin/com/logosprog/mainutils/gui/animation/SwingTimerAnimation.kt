/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

@file:Suppress("unused", "MemberVisibilityCanPrivate")

package com.logosprog.mainutils.gui.animation

import java.awt.event.ActionListener
import javax.swing.Timer

/**
 * Performs GUI com.logosprog.display.animation on a basis of {@link Timer} events.<br>
 * **Attention!** [.createTimer] must be called right
 * after this constructor invocation.
 * @param mainObject The object (normally GUI object) on which com.logosprog.display.animation is performed.
 */
abstract class SwingTimerAnimation<T> protected constructor(protected val mainObject: T) {
    protected var timer: Timer? = null

    protected var listener: AnimationListener<T>? = null

    /**
     * Initialize the [Timer] object.
     * @param interval Interval at which the timer **actionPerformed** events will be fired up.
     * *
     * @param actionListener The listener interface for receiving **actionPerformed** events
     * *                       and providing some com.logosprog.display.animation with [.mainObject].
     */
    protected fun createTimer(interval: Int = 1000, actionListener: ActionListener) {
        timer = Timer(interval, actionListener)
    }

    /**
     * Starts the timer with predefined settings.
     */
    open fun start() {
        listener!!.onAnimationStart(mainObject)
        timer?.start()
    }

    /**
     * Stops the timer.
     */
    open fun stop() {
        listener!!.onAnimationEnd(mainObject)
        timer?.stop()
    }

    fun addListener(listener: AnimationListener<T>) {
        this.listener = listener
    }
}
