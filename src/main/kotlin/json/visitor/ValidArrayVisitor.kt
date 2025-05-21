package json.visitor

import json.model.*

/**
 * Visitor implementation that checks if a JSON array is valid.
 *
 * A valid array must not contain null elements and all elements must be of the same type.
 *
 * @constructor Creates a new ValidArrayVisitor.
 */
class ValidArrayVisitor : JsonVisitor {
    private var isValid = true

    /**
     * Returns whether the visited array is valid.
     * @return True if the array is valid, false otherwise.
     */
    fun isValid(): Boolean = isValid

    /**
     * Visits a [JsonArray] and checks its validity.
     * @param arr The JSON array to validate.
     */
    override fun visit(arr: JsonArray) {
        val types = arr.elements.map { it::class }.toSet()
        if (types.contains(JsonNull::class)) isValid = false
        if (types.size > 1) isValid = false
    }

    // No validation for other types in this visitor
    override fun visit(obj: JsonObject) {}
    override fun visit(str: JsonString) {}
    override fun visit(num: JsonNumber) {}
    override fun visit(bool: JsonBoolean) {}
    override fun visit(nul: JsonNull) {}
}
