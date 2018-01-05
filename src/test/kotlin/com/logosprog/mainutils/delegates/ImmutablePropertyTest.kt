package com.logosprog.mainutils.delegates

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.io.Serializable
import java.util.*
import kotlin.test.assertEquals

/**
 * @author alog
 * @since 0.4.0
 */

data class Person(val firstName: String, val lastName: String, val age: Int) : Serializable

object ImmutablePropertyTest: Spek({
    describe("ImmutableProperty"){
        given("an object who's properties are delegated to ImmutableProperty") {
            val obj = object {

                private val immutableName = ImmutableProperty<String>()
                private val immutableDate = ImmutableProperty<Date>()
                private val immutableNumber = ImmutableProperty<Int>()
                private val immutablePerson = ImmutableProperty<Person>()

                private val nameMutator = immutableName.Mutator()
                private val dateMutator = immutableDate.Mutator()
                private val numberMutator = immutableNumber.Mutator()
                private val personMutator = immutablePerson.Mutator()

                var name by immutableName
                var date by immutableDate
                var number by immutableNumber
                var person by immutablePerson

                fun changeName(value: String) {
                    nameMutator.setValue(value)
                }

                fun changeDate(value: Date) {
                    dateMutator.setValue(value)
                }

                fun changeNumber(value: Int) {
                    numberMutator.setValue(value)
                }

                fun changePerson(value: Person) {
                    personMutator.setValue(value)
                }
            }

            val date = Date()
            val person = Person("John", "Doe", 40)

            on("set value") {


                obj.name = "name1"
                obj.date = date
                obj.number = 1
                obj.person = person

                it("should return those values") {
                    assertEquals("name1", obj.name)
                    assertEquals(date, obj.date)
                    assertEquals(1, obj.number)
                    assertEquals(person, obj.person)
                }
            }

            on("trying to change values") {
                obj.name = "name2"
                obj.date = Date()
                obj.number = 2
                obj.person = Person("Alex", "Bowie", 63)

                it("should return previous values") {
                    assertEquals("name1", obj.name)
                    assertEquals(date, obj.date)
                    assertEquals(1, obj.number)
                    assertEquals(person, obj.person)
                }
            }
            on("trying to change values from inside the object") {
                obj.changeName("name2")
                val newDate = Date()
                obj.changeDate(newDate)
                obj.changeNumber(2)
                val newPerson = Person("John", "Doe", 40)
                obj.changePerson(newPerson)

                it("should return new values") {
                    assertEquals("name2", obj.name)
                    assertEquals(newDate, obj.date)
                    assertEquals(2, obj.number)
                    assertEquals(newPerson, obj.person)
                }
            }
        }
    }
})