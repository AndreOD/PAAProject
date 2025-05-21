package json

import json.model.*
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

/**
 * Utility object for serializing Kotlin objects to JSON model representations.
 *
 * Provides methods to convert various Kotlin types (including primitives, collections, maps, enums, and data classes)
 * into a tree of [JsonElement]s suitable for further processing or serialization.
 */
object JsonSerializer {

    /**
     * Converts a Kotlin object to a [JsonElement] representation.
     *
     * Supported types:
     * - null: returns [JsonNull]
     * - [JsonElement]: returns the element itself
     * - String: returns [JsonString]
     * - Int, Double: returns [JsonNumber]
     * - Boolean: returns [JsonBoolean]
     * - List: returns [JsonArray] with recursively converted elements
     * - Map: returns [JsonObject] with string keys and recursively converted values
     * - Enum: returns [JsonString] with the enum name
     * - Data class: returns [JsonObject] with property names and recursively converted values
     *
     * @param obj The object to convert.
     * @return The corresponding [JsonElement] representation.
     * @throws IllegalArgumentException if the type is unsupported.
     */
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

