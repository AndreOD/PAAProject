package json.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class JsonNullTest {
    @Test
    fun toJsonString() {
        assertEquals("null", JsonNull.toJsonString())
    }

    @Test
    fun accept() {
        var visited = false
        val visitor = object : json.visitor.JsonVisitor {
            override fun visit(obj: JsonObject) {}
            override fun visit(arr: JsonArray) {}
            override fun visit(str: JsonString) {}
            override fun visit(num: JsonNumber) {}
            override fun visit(bool: JsonBoolean) {}
            override fun visit(nul: JsonNull) { visited = true }
        }
        JsonNull.accept(visitor)
        assertTrue(visited)
    }

    @Test
    fun toStringTest() {
        assertEquals("null", JsonNull.toString())
    }
}

