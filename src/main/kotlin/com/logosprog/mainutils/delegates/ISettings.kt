package com.logosprog.mainutils.delegates

/**
 *
 * @author alog
 */
interface ISettings<T> {
    fun getValue(key: String):T
}