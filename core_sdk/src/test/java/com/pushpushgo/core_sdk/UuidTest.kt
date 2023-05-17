package com.pushpushgo.core_sdk

import com.google.gson.JsonObject
import com.pushpushgo.core_sdk.sdk.utils.Uuid
import org.junit.Test
import org.junit.Ignore
import java.util.UUID


class UuidTest {

    @Test()
    fun `allows to extend default object`() {
        class MasterId(id: UUID) : Uuid(id)

        val mid: MasterId = Uuid.create()

        assert(mid is MasterId)
    }

    //    @Ignore("reflection is fun")
    @Test
    fun `try extend function`() {
        for (staticField in Uuid::class.java.fields) {
            println("staticField.name = ${staticField.name}")
        }

        for (declaredField in Uuid::class.java.declaredFields) {
            println("declaredField.name = ${declaredField.name}:${declaredField.type}")
        }

        for (declaredConstructor in Uuid::class.java.declaredConstructors) {
            println("declaredConstructor.name = ${declaredConstructor.name}:${declaredConstructor.isAccessible}")

            val json = JsonObject().apply {
                addProperty("name", declaredConstructor.name)
                addProperty("isAccessible", declaredConstructor.isAccessible)
                addProperty("isSynthetic", declaredConstructor.isSynthetic)
                addProperty("isVarArgs", declaredConstructor.isVarArgs)
                addProperty("modifiers", declaredConstructor.modifiers)
                addProperty(
                    "parameterTypes",
                    declaredConstructor.parameterTypes.joinToString { it.name }
                )
            }

            println(json.toString())
        }
    }

}