package json.model

import json.visitor.JsonVisitor

/**
 * Represents a JSON boolean value (true or false).
 *
 * @property value The boolean value represented by this JSON element.
 */
data class JsonBoolean(val value: Boolean) : JsonElement() {
    /**
     * Converts this JSON boolean to its JSON string representation ("true" or "false").
     * @return The JSON string representation of the boolean value.
     */
    override fun toJsonString() = value.toString()

    /**
     * Accepts a [JsonVisitor] and applies it to this boolean value.
     * @param visitor The visitor to accept.
     */
    override fun accept(visitor: JsonVisitor) = visitor.visit(this)

    /**
     * Returns a string representation of this JSON boolean for debugging purposes.
     * @return The string representation of the boolean value.
     */
    override fun toString(): String = value.toString()
}
