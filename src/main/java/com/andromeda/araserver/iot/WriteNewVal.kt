package com.andromeda.araserver.iot

import kotlin.reflect.KClass
import kotlin.reflect.full.memberFunctions

class WriteNewVal {
    fun main(classToMod: KClass<Any>, actionPair: List<String>?, any: Any) {
        classToMod.members.forEach { member ->
            if (member.name == actionPair!![0]) {
                println(member.call(any))
                println(member.parameters)

                println(member)
            }

        }
    }
}