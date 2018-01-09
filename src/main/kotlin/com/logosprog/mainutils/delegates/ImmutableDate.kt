package com.logosprog.mainutils.delegates

import java.util.*
import kotlin.reflect.KProperty

/**
 * @author alog
 */
class ImmutableDate {
    var date: Date? = null

    operator fun getValue(instance: Any, metadata: KProperty<*>): Date? {
        if (date != null) {
            return Date(date!!.time)
        }
        return null
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Date?) {
        date = if (value != null)
            Date(value.time)
        else
            null
    }
}

/**
 * @author alog
 */
class PrivateImmutableDate {
    var date: Date? = null

    operator fun getValue(instance: Any, metadata: KProperty<*>): Date? {
        if (date != null) {
            return Date(date!!.time)
        }
        return null
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, date: Date?) {
        if (this.date == null && date != null) this.date = Date(date.time)
    }

    fun setValue(date: Date?) {
        if (date != null)
            this.date = Date(date.time)
        else
            this.date = null
    }
}