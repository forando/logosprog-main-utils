package com.logosprog.mainutils.delegates

import org.apache.commons.lang3.SerializationUtils
import java.io.Serializable
import kotlin.reflect.KProperty

/**
 * Prevents properties from mutations being applied on there references
 * @author alog
 */
class ImmutableProperty<T : Serializable> {

    var obj: T? = null

    operator fun getValue(instance: Any, metadata: KProperty<*>): T? {
        if (obj != null)
            return SerializationUtils.clone(obj)
        return null
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        obj = if (value != null)
            SerializationUtils.clone(value)
        else
            null
    }
}

/**
 * Prevents properties from mutations being applied on there references.
 * This delegate also prevents any mutation from outside an object that possesses a property.
 * @author alog
 */
class PrivateImmutableProperty<T : Serializable> {

    var obj: T? = null

    operator fun getValue(instance: Any, metadata: KProperty<*>): T? {
        if (obj != null)
            return SerializationUtils.clone(obj)
        return null
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        if (obj == null && value != null) obj = SerializationUtils.clone(value)
    }

    fun setValue(value: T?) {
        obj = if (value != null)
            SerializationUtils.clone(value)
        else
            null
    }
}
