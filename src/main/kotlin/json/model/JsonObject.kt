package json.model

import json.visitor.JsonVisitor

/**
 * Represents a JSON object, which is a collection of key-value pairs where keys are strings and values are [JsonElement]s.
 *
 * @property properties The map of property names to their corresponding JSON elements.
 */
data class JsonObject(val properties: Map<String, JsonElement>) : JsonElement() {
    /**
     * Converts this JSON object to its JSON string representation.
     * @return The JSON string representation of the object.
     */
    override fun toJsonString(): String {
        val entries = properties.entries.joinToString(", ") { (k, v) ->
            "\"${k}\": ${v.toJsonString()}"
        }
        return "{ $entries }"
    }

    /**
     * Accepts a [JsonVisitor] and applies it to this object and its properties.
     * @param visitor The visitor to accept.
     */
    override fun accept(visitor: JsonVisitor) {
        visitor.visit(this)
        properties.values.forEach { it.accept(visitor) }
    }

    /**
     * Returns a new [JsonObject] containing only properties that match the given [predicate].
     * @param predicate The function to test each property entry.
     * @return A filtered [JsonObject].
     */
    fun filter(predicate: (Map.Entry<String, JsonElement>) -> Boolean): JsonObject {
        return JsonObject(properties.filter(predicate))
    }

    /**
     * Returns a string representation of this JSON object for debugging purposes.
     * @return The string representation of the object.
     */
    override fun toString(): String {
        val stringBuilder = StringBuilder();
        stringBuilder.append("{")
        properties.forEach {
            (k, v) -> stringBuilder.append("\n $k : $v, ")
        }
        stringBuilder.append("}")
        return stringBuilder.toString();
    }

}
