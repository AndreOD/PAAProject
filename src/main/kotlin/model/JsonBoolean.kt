package model

import visitor.JsonVisitor

data class JsonBoolean(val value: Boolean) : JsonElement() {
    override fun toJsonString() = value.toString()
    override fun accept(visitor: JsonVisitor) = visitor.visit(this)

    override fun toString(): String = value.toString()
}