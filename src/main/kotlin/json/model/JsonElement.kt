package json.model

import json.visitor.JsonVisitor

sealed class JsonElement {
    abstract fun toJsonString(): String
    abstract fun accept(visitor: JsonVisitor)
}