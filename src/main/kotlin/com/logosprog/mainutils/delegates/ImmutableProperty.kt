package com.logosprog.mainutils.delegates

import org.apache.commons.lang3.SerializationUtils
import java.io.Serializable
import kotlin.reflect.KProperty

/**
 *
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
 *
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
