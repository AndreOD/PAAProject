package model

import visitor.JsonVisitor

object JsonNull : JsonElement() {
    override fun toJsonString() = "null"
    override fun accept(visitor: JsonVisitor) = visitor.visit(this)
    override fun toString(): String = null.toString()

}