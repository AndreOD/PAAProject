package json

import json.model.*
import rest.Mapping
import rest.PathParam
import rest.QueryParam
import rest.RestEngine

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


//fun main(args: Array<String>) {
//    val obj = JsonObject(
//        mapOf(
//            "id" to JsonNumber(2),
//            "name" to JsonString("John"),
//            "email" to JsonString("john@mail.com"),
//            "addresses" to JsonArray(
//                listOf(
//                    JsonString("Rua Cidade de Tavira"),
//                    JsonString("Rua de Portugal")
//                )
//            ),
//        )
//    );
//
//    val array = JsonArray(
//        listOf(
//            JsonString("Rua Cidade de Tavira"),
//            JsonString("Rua de Portugal")
//        )
//    );
//    val filteredArray = array.filter { (it as JsonString).toJsonString().contains("Cidade")   }
//    println(filteredArray);
//
//
//    val course = Course(
//        "PA", 6, listOf(
//            EvalItem("quizzes", .2, false, null),
//            EvalItem("project", .8, true, EvalType.PROJECT)
//        )
//    )
//
//    val courseString = JsonSerializer.toJsonModel(course).toString();
//
//    println(courseString);
//}

@Mapping("api")
class Controller {
    @Mapping("ints")
    fun demo(): List<Int> = listOf(1, 2, 3)

    @Mapping("pair")
    fun obj(): Pair<String, String> = Pair("um", "dois")

    @Mapping("path/{pathvar}")
    fun path(
        @PathParam pathvar: String
    ): String = pathvar + "!"

    @Mapping("args")
    fun args(
        @QueryParam n: Int,
        @QueryParam text: String
    ): Map<String, String> = mapOf(text to text.repeat(n))
}



fun main(args: Array<String>) {
    val app = RestEngine(Controller::class)
    app.start()

}



