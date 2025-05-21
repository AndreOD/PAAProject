package json.visitor

import json.model.*

/**
 * Visitor implementation that checks if a JSON object is valid.
 *
 * A valid object must not contain duplicate property names.
 *
 * @constructor Creates a new ValidationVisitor.
 */
class ValidationVisitor : JsonVisitor {
    private var isValid = true

    /**
     * Returns whether the visited object is valid.
     * @return True if the object is valid, false otherwise.
     */
    fun isValid(): Boolean = isValid

    /**
     * Visits a [JsonObject] and checks its validity.
     * @param obj The JSON object to validate.
     */
    override fun visit(obj: JsonObject) {
        val keys = obj.properties.keys
        if (keys.size != keys.toSet().size) isValid = false
    }

    // No validation for other types in this visitor
    override fun visit(arr: JsonArray) {}
    override fun visit(str: JsonString) {}
    override fun visit(num: JsonNumber) {}
    override fun visit(bool: JsonBoolean) {}
    override fun visit(nul: JsonNull) {}
}

