import model.*;
import kotlin.reflect.*

import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible


data class Course(
    val name: String,
    val credits: Int,
    val evaluation: List<EvalItem>
)


data class EvalItem(
    val name: String,
    val percentage: Double,
    val mandatory: Boolean,
    val type: EvalType?
)


enum class EvalType {
    TEST, PROJECT, EXAM
}


fun toJsonModel(obj: Any?): JsonElement {
    return when (obj) {
        null -> JsonNull
        is JsonElement -> obj
        is String -> JsonString(obj)
        is Int, is Double -> JsonNumber(obj as Number)
        is Boolean -> JsonBoolean(obj)

        is List<*> -> {
            JsonArray(obj.map { toJsonModel(it) })
        }

        is Map<*, *> -> {
            val jsonMap = obj.entries
                .filter { it.key is String }
                .associate { it.key as String to toJsonModel(it.value) }
            JsonObject(jsonMap)
        }

        is Enum<*> -> JsonString(obj.name)

        else -> {
            val kClass = obj::class
            if (!kClass.isData) {
                throw IllegalArgumentException("Unsupported type: ${obj::class}")
            }

            val props = kClass.memberProperties.associate { prop ->
                prop.isAccessible = true
                val value = prop.call(obj)
                prop.name to toJsonModel(value)
            }

            JsonObject(props)
        }
    }
}



fun main(args: Array<String>) {
    val obj = JsonObject(
        mapOf(
            "id" to JsonNumber(2),
            "name" to JsonString("John"),
            "email" to JsonString("john@mail.com"),
            "addresses" to JsonArray(
                listOf(
                    JsonString("Rua Cidade de Tavira"),
                    JsonString("Rua de Portugal")
                )
            ),
        )
    );

    val array = JsonArray(
        listOf(
            JsonString("Rua Cidade de Tavira"),
            JsonString("Rua de Portugal")
        )
    );
    val filteredArray = array.filter { (it as JsonString).toJsonString().contains("Cidade")   }
    println(filteredArray);


    val course = Course(
        "PA", 6, listOf(
            EvalItem("quizzes", .2, false, null),
            EvalItem("project", .8, true, EvalType.PROJECT)
        )
    )

    val courseStrig = toJsonModel(course).toString();

    println(courseStrig);



}

