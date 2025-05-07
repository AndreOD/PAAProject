package visitor

import model.*

interface JsonVisitor {
    fun visit(obj: JsonObject)
    fun visit(arr: JsonArray)
    fun visit(str: JsonString)
    fun visit(num: JsonNumber)
    fun visit(bool: JsonBoolean)
    fun visit(nul: JsonNull)
}