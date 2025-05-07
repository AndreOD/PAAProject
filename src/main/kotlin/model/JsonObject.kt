package model

import visitor.JsonVisitor

data class JsonObject(val properties: Map<String, JsonElement>) : JsonElement() {

    override fun toJsonString(): String {
        val entries = properties.entries.joinToString(", ") { (k, v) ->
            "\"${k}\": ${v.toJsonString()}"
        }
        return "{ $entries }"
    }

    override fun accept(visitor: JsonVisitor) {
        visitor.visit(this)
        properties.values.forEach { it.accept(visitor) }
    }

    fun filter(predicate: (Map.Entry<String, JsonElement>) -> Boolean): JsonObject {
        return JsonObject(properties.filter(predicate))
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder();
        stringBuilder.append("{")
        properties.forEach {
            (k, v) -> stringBuilder.append("\n $k : $v, ")
        }
        stringBuilder.append("}")
        return stringBuilder.toString();
    }

}