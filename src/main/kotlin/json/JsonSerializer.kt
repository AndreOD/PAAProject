package json

import json.model.*

import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible



object JsonSerializer {

    fun toJsonModel(obj: Any?): JsonElement {
        return when (obj) {
            null -> JsonNull
            is JsonElement -> obj
            is String -> JsonString(obj)
            is Int, is Double -> JsonNumber(obj as Number)
            is Boolean -> JsonBoolean(obj)

            is List<*> -> {
                JsonArray(obj.map { toJsonModel(it) })
            }

            is Map<*, *> -> {
                val jsonMap = obj.entries
                    .filter { it.key is String }
                    .associate { it.key as String to toJsonModel(it.value) }
                JsonObject(jsonMap)
            }

            is Enum<*> -> JsonString(obj.name)

            else -> {
                val kClass = obj::class
                if (!kClass.isData) {
                    throw IllegalArgumentException("Unsupported type: ${obj::class}")
                }

                val props = kClass.memberProperties.associate { prop ->
                    prop.isAccessible = true
                    val value = prop.call(obj)
                    prop.name to toJsonModel(value)
                }

                JsonObject(props)
            }
        }
    }


}



