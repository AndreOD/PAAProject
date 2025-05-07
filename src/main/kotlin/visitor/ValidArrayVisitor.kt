package visitor

import model.*;

class ValidArrayVisitor : JsonVisitor {
    private var isValid = true

    fun isValid(): Boolean = isValid

    override fun visit(arr: JsonArray) {
        val types = arr.elements.map { it::class }.toSet()
        if (types.contains(JsonNull::class)) isValid = false
        if (types.size > 1) isValid = false
    }

    override fun visit(obj: JsonObject) {}
    override fun visit(str: JsonString) {}
    override fun visit(num: JsonNumber) {}
    override fun visit(bool: JsonBoolean) {}
    override fun visit(nul: JsonNull) {}
}