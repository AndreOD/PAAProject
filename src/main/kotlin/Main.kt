import model.*;


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
}

