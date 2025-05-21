package json.model

import json.visitor.JsonVisitor

/**
 * Represents a JSON string value.
 *
 * @property value The string value represented by this JSON element.
 */
data class JsonString(val value: String) : JsonElement() {
    /**
     * Converts this JSON string to its JSON string representation (with quotes).
     * @return The JSON string representation of the string value.
     */
    override fun toJsonString() = "\"$value\""

    /**
     * Accepts a [JsonVisitor] and applies it to this string value.
     * @param visitor The visitor to accept.
     */
    override fun accept(visitor: JsonVisitor) = visitor.visit(this)

    /**
     * Returns a string representation of this JSON string for debugging purposes.
     * @return The string value.
     */
    override fun toString(): String = value
}
