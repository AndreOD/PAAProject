package json.visitor

import json.model.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ValidArrayVisitorTest {
    @Test
    fun validArrayWithSameType() {
        val arr = JsonArray(listOf(JsonNumber(1), JsonNumber(2)))
        val visitor = ValidArrayVisitor()
        arr.accept(visitor)
        assertTrue(visitor.isValid())
    }

    @Test
    fun invalidArrayWithNull() {
        val arr = JsonArray(listOf(JsonNumber(1), JsonNull))
        val visitor = ValidArrayVisitor()
        arr.accept(visitor)
        assertFalse(visitor.isValid())
    }

    @Test
    fun invalidArrayWithDifferentTypes() {
        val arr = JsonArray(listOf(JsonNumber(1), JsonString("foo")))
        val visitor = ValidArrayVisitor()
        arr.accept(visitor)
        assertFalse(visitor.isValid())
    }
}

class ValidationVisitorTest {
    @Test
    fun validObjectWithUniqueKeys() {
        val obj = JsonObject(mapOf("a" to JsonNumber(1), "b" to JsonNumber(2)))
        val visitor = ValidationVisitor()
        obj.accept(visitor)
        assertTrue(visitor.isValid())
    }

    @Test
    fun validObjectWithNoKeys() {
        val obj = JsonObject(emptyMap())
        val visitor = ValidationVisitor()
        obj.accept(visitor)
        assertTrue(visitor.isValid())
    }
}

