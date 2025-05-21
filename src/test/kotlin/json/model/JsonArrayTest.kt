package json.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class JsonArrayTest {
    @Test
    fun toJsonString() {
        val arr = JsonArray(listOf(JsonString("foo"), JsonNumber(42), JsonBoolean(true)))
        assertEquals("[ \"foo\", 42, true ]", arr.toJsonString())
        assertEquals("[  ]", JsonArray(emptyList()).toJsonString())
    }

    @Test
    fun accept() {
        val arr = JsonArray(listOf(JsonString("foo"), JsonNumber(42)))
        val visited = mutableListOf<String>()
        val visitor = object : json.visitor.JsonVisitor {
            override fun visit(obj: JsonObject) {}
            override fun visit(arr: JsonArray) { visited.add("array") }
            override fun visit(str: JsonString) { visited.add("string") }
            override fun visit(num: JsonNumber) { visited.add("number") }
            override fun visit(bool: JsonBoolean) {}
            override fun visit(nul: JsonNull) {}
        }
        arr.accept(visitor)
        assertTrue(visited.contains("array"))
        assertTrue(visited.contains("string"))
        assertTrue(visited.contains("number"))
    }

    @Test
    fun filter() {
        val arr = JsonArray(listOf(JsonNumber(1), JsonNumber(2), JsonString("foo")))
        val filtered = arr.filter { it is JsonNumber }
        assertEquals(2, filtered.elements.size)
        assertTrue(filtered.elements.all { it is JsonNumber })
    }

    @Test
    fun map() {
        val arr = JsonArray(listOf(JsonNumber(1), JsonNumber(2)))
        val mapped = arr.map {
            if (it is JsonNumber) JsonNumber((it.value.toInt() * 2)) else it
        }
        assertEquals(2, mapped.elements.size)
        assertEquals(2, (mapped.elements[0] as JsonNumber).value)
        assertEquals(4, (mapped.elements[1] as JsonNumber).value)
    }
}

