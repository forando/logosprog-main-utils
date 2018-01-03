package com.logosprog.mainutils.files.xml.builders

import com.logosprog.mainutils.files.builders.ResourceInputStreamBuilder
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertNotNull
import kotlin.test.assertNull

object inputStreamBuilder : ResourceInputStreamBuilder(){
    override val objectClass: Class<*>
        get() = this.javaClass

}

object XmlBuilderTest: Spek({
    describe("XmlBuilder") {
        given("an instance with valid inputStream") {
            val xmlBuilder = XmlBuilder(inputStreamBuilder.build("data.xml"))
            it("should not be null") {
                assertNotNull(xmlBuilder)
            }
            on("building XmlDocument") {
                val doc = xmlBuilder.xmlDocument
                it("should not be null") {
                    assertNotNull(doc)
                }
            }
        }
        given("an instance with null inputStream") {
            val xmlBuilder = XmlBuilder(null)
            it("should not be null") {
                assertNotNull(xmlBuilder)
            }
            on("building XmlDocument") {
                val doc = xmlBuilder.xmlDocument
                it("should be null") {
                    assertNull(doc)
                }
            }
        }
    }

})