package com.logosprog.mainutils.files.xml.parser

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.test.assertNotNull

object NodeManagerTest: Spek({
    describe("NodeManager") {
        given("NodeManager") {

            val fileBuilder = object {
                fun getFile() = this.javaClass.classLoader.getResource("data_mediacontent.xml").file
            }
            val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fileBuilder.getFile())

            val manager = NodeManager()

            on("getNode with Document as an argument") {
                val node = manager.getNode(doc, "videos", "currentvideopath")
                it("should not be null") {
                    assertNotNull(node)
                }
            }
        }
    }
})