package com.logosprog.mainutils.delegates

import kotlin.reflect.KProperty

/**
 *
 * @author alog
 */
class SettingsSelector<out T>(private val selector: String, private val settings: ISettings<T>){
    operator fun getValue(instance: Any, metadata: KProperty<*>): T {
        return settings.getValue("$selector.${metadata.name}")
    }
}