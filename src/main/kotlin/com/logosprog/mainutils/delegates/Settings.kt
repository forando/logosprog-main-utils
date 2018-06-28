package com.logosprog.mainutils.delegates

import com.logosprog.mainutils.files.properties.builders.PropertiesBuilder
import java.io.InputStream

/**
 *
 * @author alog
 */
class Settings(inStream: InputStream): PropertiesBuilder(inStream), ISettings<String> {

    operator fun get(selector: String) = SettingsSelector(selector, this)

    override fun getValue(key: String): String = properties.getProperty(key) ?: throw UnsupportedOperationException("Value for $key not found")
}
