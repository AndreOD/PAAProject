package json.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class JsonObjectTest {
    @Test
    fun toJsonString() {
        val obj = JsonObject(mapOf("a" to JsonNumber(1), "b" to JsonString("foo")))
        assertEquals("{ \"a\": 1, \"b\": \"foo\" }", obj.toJsonString())
        assertEquals("{  }", JsonObject(emptyMap()).toJsonString())
    }

    @Test
    fun accept() {
        val obj = JsonObject(mapOf("x" to JsonNumber(2), "y" to JsonBoolean(true)))
        val visited = mutableListOf<String>()
        val visitor = object : json.visitor.JsonVisitor {
            override fun visit(obj: JsonObject) { visited.add("object") }
            override fun visit(arr: JsonArray) {}
            override fun visit(str: JsonString) {}
            override fun visit(num: JsonNumber) { visited.add("number") }
            override fun visit(bool: JsonBoolean) { visited.add("boolean") }
            override fun visit(nul: JsonNull) {}
        }
        obj.accept(visitor)
        assertTrue(visited.contains("object"))
        assertTrue(visited.contains("number"))
        assertTrue(visited.contains("boolean"))
    }

    @Test
    fun filter() {
        val obj = JsonObject(mapOf("a" to JsonNumber(1), "b" to JsonString("foo")))
        val filtered = obj.filter { it.value is JsonNumber }
        assertEquals(1, filtered.properties.size)
        assertTrue(filtered.properties.containsKey("a"))
    }

    @Test
    fun toStringTest() {
        val obj = JsonObject(mapOf("a" to JsonNumber(1)))
        assertTrue(obj.toString().contains("a"))
    }
}

