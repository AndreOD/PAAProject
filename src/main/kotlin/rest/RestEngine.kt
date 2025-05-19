package rest

import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.findAnnotation
import com.sun.net.httpserver.HttpServer
import com.sun.net.httpserver.HttpExchange
import java.net.InetSocketAddress
import java.io.OutputStream


@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class Mapping(val path: String)

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Query(val name: String)

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Path(val name: String)

class RestEngine(private val controllerClass : KClass<Any>) {

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
            val endpointPath = "${rootPath.trim()}/${mappingAnnotation.path.trim()}";

            server.createContext(endpointPath) { exchange ->
                val response = "Hello from Kotlin!"
                exchange.sendResponseHeaders(200, response.toByteArray().size.toLong())
                exchange.responseBody.use { it.write(response.toByteArray()) }

                val pathTemplateParts = endpointPath.split("/");
                val requestPathParts = exchange.requestURI.path.split("/")
                // users/1/info
                val requestQueryParts : Map<String, String> = exchange.requestURI.path.substringAfter("?").split("&")
                    .associate {
                    it.substringBefore("=") to it.substringAfter("=")
                }


                val params : List<Any> = memberFunction.parameters.map {
                    param ->
                    val pathAnnotation = param.findAnnotation<Path>();
                    val queryAnnotation = param.findAnnotation<Query>();
                    when {
                        pathAnnotation != null -> {;
                            val pos = pathTemplateParts.indexOf(pathAnnotation.name);
                            requestPathParts[pos]
                        }
                        queryAnnotation != null -> {
                            requestQueryParts[queryAnnotation.name] ?: throw IllegalArgumentException("Unsupported path parameter: ${param.name}")
                        }
                        else -> {
                             throw IllegalArgumentException("Unsupported path parameter: ${param.name}")
                        }
                    }
                }

                val retunedValue = memberFunction.call(params)

            }

        }



    }

}

