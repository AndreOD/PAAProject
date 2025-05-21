package json.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class JsonNumberTest {
    @Test
    fun toJsonString() {
        assertEquals("42", JsonNumber(42).toJsonString())
        assertEquals("3.14", JsonNumber(3.14).toJsonString())
    }

    @Test
    fun accept() {
        val visited = mutableListOf<Number>()
        val visitor = object : json.visitor.JsonVisitor {
            override fun visit(obj: JsonObject) {}
            override fun visit(arr: JsonArray) {}
            override fun visit(str: JsonString) {}
            override fun visit(num: JsonNumber) { visited.add(num.value) }
            override fun visit(bool: JsonBoolean) {}
            override fun visit(nul: JsonNull) {}
        }
        JsonNumber(7).accept(visitor)
        assertTrue(visited.contains(7))
    }

    @Test
    fun toStringTest() {
        assertEquals("100", JsonNumber(100).toString())
    }
}

