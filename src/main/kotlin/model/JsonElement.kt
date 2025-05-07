package model

import visitor.JsonVisitor

sealed class JsonElement {
    abstract fun toJsonString(): String
    abstract fun accept(visitor: JsonVisitor)
}