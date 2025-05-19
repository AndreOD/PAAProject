package rest

import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import com.sun.net.httpserver.HttpServer
import json.JsonSerializer
import java.net.InetSocketAddress
import kotlin.reflect.KParameter.Kind
import kotlin.reflect.full.cast
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.primaryConstructor


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

        controllerClass.declaredFunctions.forEach {
            memberFunction ->
            val mappingAnnotation = memberFunction.findAnnotation<Mapping>() ?: return;
            val endpointPath = "/${rootPath.trim()}/${mappingAnnotation.path.trim()}";
            println(endpointPath)
            server.createContext(endpointPath) { exchange ->

                val pathTemplateParts = endpointPath.split("/");
                val requestPathParts = exchange.requestURI.path.split("/")
                // users/1/info
                val requestQueryParts : Map<String, String> = exchange.requestURI.query.substringAfter("?").split("&")
                    .associate {
                    it.substringBefore("=") to it.substringAfter("=")
                }


                val params : List<*> = memberFunction.parameters.map {
                    param ->
                    val pathAnnotation = param.findAnnotation<PathParam>();
                    val queryAnnotation = param.findAnnotation<QueryParam>();
                    val kclass: KClass<*> = param.type.classifier as KClass<*>;

                    when {
                        param.kind == Kind.INSTANCE -> controllerClass.primaryConstructor?.call()
                        pathAnnotation != null -> {;
                            val pos = pathTemplateParts.indexOf("{${param.name}}");
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
        server.start()

    }

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

