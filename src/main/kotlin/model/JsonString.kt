package model

import visitor.JsonVisitor

data class JsonString(val value: String) : JsonElement() {
    override fun toJsonString() = "\"$value\""
    override fun accept(visitor: JsonVisitor) = visitor.visit(this)
    override fun toString(): String = value
}