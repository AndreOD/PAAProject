package json.model

import json.visitor.JsonVisitor

/**
 * Represents a JSON number value.
 *
 * @property value The numeric value represented by this JSON element.
 */
data class JsonNumber(val value: Number) : JsonElement() {
    /**
     * Converts this JSON number to its JSON string representation.
     * @return The JSON string representation of the number value.
     */
    override fun toJsonString() = value.toString()

    /**
     * Accepts a [JsonVisitor] and applies it to this number value.
     * @param visitor The visitor to accept.
     */
    override fun accept(visitor: JsonVisitor) = visitor.visit(this)

    /**
     * Returns a string representation of this JSON number for debugging purposes.
     * @return The string representation of the number value.
     */
    override fun toString(): String = value.toString()
}
