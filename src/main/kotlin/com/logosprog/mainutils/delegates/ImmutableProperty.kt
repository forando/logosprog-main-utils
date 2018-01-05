package com.logosprog.mainutils.delegates

import org.apache.commons.lang3.SerializationUtils
import java.io.Serializable
import kotlin.reflect.KProperty

/**
 * @author alog
 * @since 0.4.0
 */
class ImmutableProperty<T : Serializable> {

    var obj: T? = null

    operator fun getValue(instance: Any, metadata: KProperty<*>) = SerializationUtils.clone(obj)

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        if (obj == null) obj = value
    }

    operator fun plusAssign(value: T) {
        obj = value
    }


    inner class Mutator {

        fun setValue(value: T) {
            obj = value
        }
    }

}
