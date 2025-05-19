package json.model

import json.visitor.JsonVisitor

data class JsonNumber(val value: Number) : JsonElement() {
    override fun toJsonString() = value.toString()
    override fun accept(visitor: JsonVisitor) = visitor.visit(this)
    override fun toString(): String = value.toString()
}