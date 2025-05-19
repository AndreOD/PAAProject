package rest

import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.findAnnotation
import com.sun.net.httpserver.HttpServer
import json.JsonSerializer
import java.net.InetSocketAddress


@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class Mapping(val path: String)

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class QueryParam

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class PathParam

class RestEngine(private val controllerClass : KClass<*>) {

    fun start(port: Int = 8080) {
        try {

        }catch (e: Exception) {
            error("Error occurred while starting Rest Engine: ${e.message.toString()}")
        }
        val server = HttpServer.create(InetSocketAddress(port), 0);

        val rootPath = (controllerClass.findAnnotation<Mapping>()?.path) ?: "";

        controllerClass.declaredMemberFunctions.forEach {
            memberFunction ->
            val mappingAnnotation = memberFunction.findAnnotation<Mapping>() ?: return;
            val endpointPath = "/${rootPath.trim()}/${mappingAnnotation.path.trim()}";
            println(endpointPath)
            server.createContext(endpointPath) { exchange ->

                val pathTemplateParts = endpointPath.split("/");
                val requestPathParts = exchange.requestURI.path.split("/")
                // users/1/info
                val requestQueryParts : Map<String, String> = exchange.requestURI.path.substringAfter("?").split("&")
                    .associate {
                    it.substringBefore("=") to it.substringAfter("=")
                }


                val params : List<*> = memberFunction.parameters.map {
                    param ->
                    val pathAnnotation = param.findAnnotation<PathParam>();
                    val queryAnnotation = param.findAnnotation<QueryParam>();
                    when {
                        pathAnnotation != null -> {;
                            val pos = pathTemplateParts.indexOf("{${param.name}}");
                            requestPathParts[pos]
                        }
                        queryAnnotation != null -> {
                            requestQueryParts[param.name] ?: throw IllegalArgumentException("Unsupported query parameter: ${param.name}")
                        }
                        else -> {
                             throw IllegalArgumentException("Unsupported path parameter: ${param.name}")
                        }
                    }
                }

                val returnedValue = memberFunction.call(params)

                val response = JsonSerializer.toJsonModel(returnedValue).toJsonString();
                exchange.sendResponseHeaders(200, response.toByteArray().size.toLong())
                exchange.responseBody.use { it.write(response.toByteArray()) }

            }
        }
        server.start()

    }

}

