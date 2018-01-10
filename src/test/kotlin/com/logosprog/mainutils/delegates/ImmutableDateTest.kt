package com.logosprog.mainutils.delegates

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

/**
 * @author alog
 */
object ImmutableDateTest : Spek({
    describe("PrivateImmutableDate delegate") {
        given("an object who's date property is delegated to PrivateImmutableDate") {
            val obj = object {

                private val immutableDate = PrivateImmutableDate()

                var date by immutableDate

                fun changeDate(value: Date) {
                    immutableDate.setValue(value)
                }
            }

            val date = Date()

            on("set value") {
                obj.date = date

                it("should return that value") {
                    assertEquals(date, obj.date)
                }
            }

            on("trying to reset date") {
                obj.date = Date()

                it("should return previous date") {
                    assertEquals(date, obj.date)
                }
            }
            on("trying to change properties in supplied date") {
                date.time = Date().time

                it("should return previous values") {
                    assertNotEquals(date, obj.date)
                }
            }
            on("trying to change date from inside the object") {
                val newDate = Date()
                obj.changeDate(newDate)

                it("should return new date") {
                    assertEquals(newDate, obj.date)
                }
            }
        }
    }
    describe("ImmutableDate delegate") {
        given("an object who's date property is delegated to ImmutableDate") {
            val obj = object {

                private val immutableDate = ImmutableDate()

                var date by immutableDate
            }

            val date = Date()

            on("set value") {
                obj.date = date

                it("should return that value") {
                    assertEquals(date, obj.date)
                }
            }

            on("trying to change properties in supplied date") {
                date.time = Date().time

                it("should return previous values") {
                    assertNotEquals(date, obj.date)
                }
            }

            on("trying to reset date") {
                val newDate = Date()
                obj.date = newDate

                it("should return new date") {
                    assertEquals(newDate, obj.date)
                }
            }
        }
    }
})