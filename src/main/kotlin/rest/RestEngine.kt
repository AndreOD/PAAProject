package rest

import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import com.sun.net.httpserver.HttpServer
import json.JsonSerializer
import java.net.InetSocketAddress
import kotlin.reflect.KParameter.Kind
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.primaryConstructor

/**
 * Annotation for mapping HTTP requests onto classes or functions.
 * @property path The path to map to.
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class Mapping(val path: String)

/**
 * Annotation to mark a function parameter as a query parameter in the HTTP request.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class QueryParam

/**
 * Annotation to mark a function parameter as a path parameter in the HTTP request.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class PathParam

/**
 * A simple REST engine that maps annotated controller classes and functions to HTTP endpoints.
 *
 * @param controllerClasses The controller classes to register with the REST engine.
 */
class RestEngine(private vararg val controllerClasses: KClass<*>) {

    private var server: HttpServer = HttpServer.create();

    /**
     * Starts the REST engine on the specified port.
     *
     * @param port The port to listen on. Defaults to 8080.
     */
    fun start(port: Int = 8080) {
        try {

        } catch (e: Exception) {
            error("Error occurred while starting Rest Engine: ${e.message.toString()}")
        }
        server = HttpServer.create(InetSocketAddress(port), 0);
        controllerClasses.forEach {
            val rootPath = (it.findAnnotation<Mapping>()?.path) ?: "";

            it.declaredFunctions.forEach { memberFunction ->
                val mappingAnnotation = memberFunction.findAnnotation<Mapping>() ?: return;
                val endpointPath =
                    "/${rootPath.trim()}/${mappingAnnotation.path.trim()}".split("/").filter { !it.contains("{") }
                        .joinToString("/");
                server.createContext(endpointPath) { exchange ->

                    val pathTemplateParts = endpointPath.substringBefore("?").split("/");
                    val requestPathParts = exchange.requestURI.path.substringBefore("?").split("/")

                    val requestQueryParts: Map<String, String> =
                        (exchange.requestURI.query ?: "").substringAfter("?").split("&")
                            .associate {
                                it.substringBefore("=") to it.substringAfter("=")
                            }

                    val params: List<*> = memberFunction.parameters.map { param ->
                        val pathAnnotation = param.findAnnotation<PathParam>();
                        val queryAnnotation = param.findAnnotation<QueryParam>();
                        val kclass: KClass<*> = param.type.classifier as KClass<*>;

                        when {
                            param.kind == Kind.INSTANCE -> it.primaryConstructor?.call()
                            pathAnnotation != null -> {;
                                //val pos = pathTemplateParts.indexOf("{${param.name}}");
                                val pos = pathTemplateParts.size
                                convertStringToPrimitiveType(requestPathParts[pos], kclass)
                            }

                            queryAnnotation != null -> {
                                convertStringToPrimitiveType(requestQueryParts[param.name] ?: "", kclass)
                            }

                            else -> {
                                throw IllegalArgumentException("Unsupported path parameter: ${param.name}")
                            }
                        }
                    }

                    val returnedValue = memberFunction.call(*params.toTypedArray());

                    val response = JsonSerializer.toJsonModel(returnedValue).toJsonString();
                    exchange.sendResponseHeaders(200, response.toByteArray().size.toLong())
                    exchange.responseBody.write(response.toByteArray())

                }
            }
        }

        server.start()

    }

    /**
     * Stops the REST engine and shuts down the HTTP server.
     */
    fun stop() {
        // Implement server shutdown logic if needed
        server.stop(0);
    }

    /**
     * Converts a string value to the specified primitive type.
     *
     * @param value The string value to convert.
     * @param kClass The target Kotlin class type.
     * @return The converted value as the specified type, or null if conversion fails.
     * @throws IllegalArgumentException If the type is unsupported.
     */
    fun <T : Any> convertStringToPrimitiveType(value: String, kClass: KClass<T>): T? {
        return when (kClass) {
            Int::class -> value.toIntOrNull() as T?
            Double::class -> value.toDoubleOrNull() as T?
            Boolean::class -> value.toBooleanStrictOrNull() as T?
            Long::class -> value.toLongOrNull() as T?
            Float::class -> value.toFloatOrNull() as T?
            String::class -> value as T?
            else -> {
                // For non-primitive types, try using a constructor (if available)
                throw IllegalArgumentException("Unsupported type ${kClass.simpleName}")
            }
        }
    }

}