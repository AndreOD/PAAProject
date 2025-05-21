package json.model

import json.visitor.JsonVisitor

/**
 * Represents the JSON null value.
 *
 * This is a singleton object representing the null value in JSON.
 */
object JsonNull : JsonElement() {
    /**
     * Converts this JSON null to its JSON string representation ("null").
     * @return The JSON string representation of null.
     */
    override fun toJsonString() = "null"

    /**
     * Accepts a [JsonVisitor] and applies it to this null value.
     * @param visitor The visitor to accept.
     */
    override fun accept(visitor: JsonVisitor) = visitor.visit(this)

    /**
     * Returns a string representation of this JSON null for debugging purposes.
     * @return The string representation of null.
     */
    override fun toString(): String = null.toString()
}
