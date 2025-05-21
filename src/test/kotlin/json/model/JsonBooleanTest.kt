package json.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class JsonBooleanTest {
    @Test
    fun toJsonString() {
        assertEquals("true", JsonBoolean(true).toJsonString())
        assertEquals("false", JsonBoolean(false).toJsonString())
    }

    @Test
    fun accept() {
        val visited = mutableListOf<Boolean>()
        val visitor = object : json.visitor.JsonVisitor {
            override fun visit(obj: JsonObject) {}
            override fun visit(arr: JsonArray) {}
            override fun visit(str: JsonString) {}
            override fun visit(num: JsonNumber) {}
            override fun visit(bool: JsonBoolean) { visited.add(bool.value) }
            override fun visit(nul: JsonNull) {}
        }
        JsonBoolean(true).accept(visitor)
        assertTrue(visited.contains(true))
    }

    @Test
    fun toStringTest() {
        assertEquals("true", JsonBoolean(true).toString())
        assertEquals("false", JsonBoolean(false).toString())
    }
}

