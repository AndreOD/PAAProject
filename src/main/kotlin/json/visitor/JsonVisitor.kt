package json.visitor

import json.model.*

/**
 * Visitor interface for traversing and operating on different types of JSON elements.
 *
 * Implementations of this interface can perform operations on each specific JSON element type
 * (object, array, string, number, boolean, null) by overriding the corresponding visit method.
 */
interface JsonVisitor {
    /**
     * Visits a [JsonObject] element.
     * @param obj The JSON object to visit.
     */
    fun visit(obj: JsonObject)

    /**
     * Visits a [JsonArray] element.
     * @param arr The JSON array to visit.
     */
    fun visit(arr: JsonArray)

    /**
     * Visits a [JsonString] element.
     * @param str The JSON string to visit.
     */
    fun visit(str: JsonString)

    /**
     * Visits a [JsonNumber] element.
     * @param num The JSON number to visit.
     */
    fun visit(num: JsonNumber)

    /**
     * Visits a [JsonBoolean] element.
     * @param bool The JSON boolean to visit.
     */
    fun visit(bool: JsonBoolean)

    /**
     * Visits a [JsonNull] element.
     * @param nul The JSON null to visit.
     */
    fun visit(nul: JsonNull)
}
