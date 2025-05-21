package json

import json.model.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

enum class TestEnum { FOO, BAR }
data class TestData(val a: Int, val b: String)

class JsonSerializerTest {
    @Test
    fun serializeNull() {
        assertEquals(JsonNull, JsonSerializer.toJsonModel(null))
    }

    @Test
    fun serializePrimitiveTypes() {
        assertEquals(JsonString("abc"), JsonSerializer.toJsonModel("abc"))
        assertEquals(JsonNumber(123), JsonSerializer.toJsonModel(123))
        assertEquals(JsonNumber(3.14), JsonSerializer.toJsonModel(3.14))
        assertEquals(JsonBoolean(true), JsonSerializer.toJsonModel(true))
    }

    @Test
    fun serializeList() {
        val list = listOf(1, 2, 3)
        val json = JsonSerializer.toJsonModel(list)
        assertTrue(json is JsonArray)
        assertEquals(3, (json as JsonArray).elements.size)
        assertEquals(JsonNumber(1), json.elements[0])
    }

    @Test
    fun serializeMap() {
        val map = mapOf("x" to 1, "y" to 2)
        val json = JsonSerializer.toJsonModel(map)
        assertTrue(json is JsonObject)
        assertEquals(JsonNumber(1), (json as JsonObject).properties["x"])
    }

    @Test
    fun serializeEnum() {
        assertEquals(JsonString("FOO"), JsonSerializer.toJsonModel(TestEnum.FOO))
    }

    @Test
    fun serializeDataClass() {
        val data = TestData(5, "bar")
        val json = JsonSerializer.toJsonModel(data)
        assertTrue(json is JsonObject)
        val props = (json as JsonObject).properties
        assertEquals(JsonNumber(5), props["a"])
        assertEquals(JsonString("bar"), props["b"])
    }

    @Test
    fun serializeJsonElement() {
        val elem = JsonNumber(99)
        assertEquals(elem, JsonSerializer.toJsonModel(elem))
    }
}

