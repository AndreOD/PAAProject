package json.visitor

import json.model.*

class ValidationVisitor : JsonVisitor {
    private var isValid = true

    fun isValid(): Boolean = isValid

    override fun visit(obj: JsonObject) {
        val keys = obj.properties.keys
        if (keys.size != keys.toSet().size) isValid = false
    }

    override fun visit(arr: JsonArray) {}
    override fun visit(str: JsonString) {}
    override fun visit(num: JsonNumber) {}
    override fun visit(bool: JsonBoolean) {}
    override fun visit(nul: JsonNull) {}
}

