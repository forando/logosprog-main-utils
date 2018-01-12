/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

@file:Suppress("unused")

package com.logosprog.mainutils.gui.animation

/**
 * Specifies basic com.logosprog.display.animation events.
 */
interface AnimationListener<T> {
    fun onAnimationStart(obj: T?)
    fun onAnimationEnd(obj: T?)
    fun reDraw()
}