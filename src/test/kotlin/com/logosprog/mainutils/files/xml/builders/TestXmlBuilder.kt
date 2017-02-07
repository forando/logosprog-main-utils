package com.logosprog.mainutils.files.xml.builders

import com.logosprog.mainutils.files.builders.ResourceInputStreamBuilder
import org.junit.Assert.*
import org.junit.Test

class TestXmlBuilder{

    object inputStreamBuilder : ResourceInputStreamBuilder(){
        override val objectClass: Class<*>
            get() = this.javaClass

    }

    @Test fun CreateBuilderWithInputStream(){
        val xmlBuilder = XmlBuilder(inputStreamBuilder.build("data.xml"))
        assertNotNull("xmlBuilder class is NULL", xmlBuilder)
    }

    @Test fun ConvertInputStreamIntoDocument(){
        val xmlDocument = XmlBuilder(inputStreamBuilder.build("data.xml")).xmlDocument
        assertNotNull("xmlDocument is NULL", xmlDocument)
    }
}