package json.model

import json.visitor.JsonVisitor

/**
 * Represents a JSON array, which is an ordered collection of [JsonElement]s.
 *
 * @property elements The list of elements contained in this JSON array.
 */
data class JsonArray(val elements: List<JsonElement>) : JsonElement() {
    /**
     * Converts this JSON array to its JSON string representation.
     * @return The JSON string representation of the array.
     */
    override fun toJsonString(): String {
        return "[ ${elements.joinToString(", ") { it.toJsonString() } } ]"
    }

    /**
     * Accepts a [JsonVisitor] and applies it to this array and its elements.
     * @param visitor The visitor to accept.
     */
    override fun accept(visitor: JsonVisitor) {
        visitor.visit(this)
        elements.forEach { it.accept(visitor) }
    }

    /**
     * Returns a new [JsonArray] containing only elements that match the given [predicate].
     * @param predicate The function to test each element.
     * @return A filtered [JsonArray].
     */
    fun filter(predicate: (JsonElement) -> Boolean): JsonArray {
        return JsonArray(elements.filter(predicate))
    }

    /**
     * Returns a new [JsonArray] with each element transformed by the given [transform] function.
     * @param transform The function to apply to each element.
     * @return A mapped [JsonArray].
     */
    fun map(transform: (JsonElement) -> JsonElement): JsonArray {
        return JsonArray(elements.map(transform))
    }

    /**
     * Returns a string representation of this JSON array for debugging purposes.
     * @return The string representation of the array.
     */
    override fun toString(): String {
        return "[ ${elements.joinToString(", ") { it.toString() } } ]"
    }
}
