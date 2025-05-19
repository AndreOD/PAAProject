package json.model

import json.visitor.JsonVisitor

data class JsonArray(val elements: List<JsonElement>) : JsonElement() {
    override fun toJsonString(): String {
        return "[ ${elements.joinToString(", ") { it.toJsonString() } } ]"
    }

    override fun accept(visitor: JsonVisitor) {
        visitor.visit(this)
        elements.forEach { it.accept(visitor) }
    }

    fun filter(predicate: (JsonElement) -> Boolean): JsonArray {
        return JsonArray(elements.filter(predicate))
    }

    fun map(transform: (JsonElement) -> JsonElement): JsonArray {
        return JsonArray(elements.map(transform))
    }

    override fun toString(): String {
        return "[ ${elements.joinToString(", ") { it.toString() } } ]"
    }
}