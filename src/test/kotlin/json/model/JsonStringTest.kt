package json.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class JsonStringTest {
    @Test
    fun toJsonString() {
        assertEquals("\"hello\"", JsonString("hello").toJsonString())
        assertEquals("\"\"", JsonString("").toJsonString())
    }

    @Test
    fun accept() {
        val visited = mutableListOf<String>()
        val visitor = object : json.visitor.JsonVisitor {
            override fun visit(obj: JsonObject) {}
            override fun visit(arr: JsonArray) {}
            override fun visit(str: JsonString) { visited.add(str.value) }
            override fun visit(num: JsonNumber) {}
            override fun visit(bool: JsonBoolean) {}
            override fun visit(nul: JsonNull) {}
        }
        JsonString("foo").accept(visitor)
        assertTrue(visited.contains("foo"))
    }

    @Test
    fun toStringTest() {
        assertEquals("bar", JsonString("bar").toString())
    }
}

