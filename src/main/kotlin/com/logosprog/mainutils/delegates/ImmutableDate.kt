package com.logosprog.mainutils.delegates

import java.util.*
import kotlin.reflect.KProperty

/**
 * Prevents date property from mutations being applied on its references
 * @author alog
 */
class ImmutableDate {
    private var date: Date? = null

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
 * Prevents date property from mutations being applied on its references.
 * This delegate also prevents any mutation from outside an object that possesses the property.
 * @author alog
 */
class PrivateImmutableDate {
    private var date: Date? = null

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