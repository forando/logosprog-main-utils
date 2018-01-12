package com.logosprog.mainutils.files.builders

/**
 *
 * @author alog
 */
interface ISettings<T> {
    fun getValue(key: String):T
}