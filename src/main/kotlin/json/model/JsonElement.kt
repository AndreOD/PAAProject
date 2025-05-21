package json.model

import json.visitor.JsonVisitor

/**
 * The base sealed class for all JSON element types.
 *
 * All specific JSON types (object, array, string, number, boolean, null) should inherit from this class.
 * Provides the interface for converting to a JSON string and accepting a visitor.
 */
sealed class JsonElement {
    /**
     * Converts this element to its JSON string representation.
     * @return The JSON string representation of this element.
     */
    abstract fun toJsonString(): String

    /**
     * Accepts a [JsonVisitor] and applies it to this element.
     * @param visitor The visitor to accept.
     */
    abstract fun accept(visitor: JsonVisitor)
}
