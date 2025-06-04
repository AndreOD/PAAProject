package rest

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.HttpUrl

/**
 * Test controller with example endpoints for RestEngine.
 */
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

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RestEngineTest {
    private val port = 18080
    private val baseUrl = "http://localhost:$port/api"
    private val client = OkHttpClient()
    private var restEngine = RestEngine()

    @BeforeEach
    fun setup() {
        restEngine = RestEngine(Controller::class)
        restEngine.start(port)
        // Wait for server to start
        Thread.sleep(500)
    }

    @AfterEach
    fun tearDown() {
        restEngine.stop()
        // Wait for server to stop
        Thread.sleep(500)
    }

    @Test
    fun `GET ints returns list of ints`() {
        val req = Request.Builder()
            .url("$baseUrl/ints")
            .build()
        client.newCall(req).execute().use { resp ->
            assertEquals(200, resp.code)
            assertEquals("[ 1, 2, 3 ]", resp.body.string())
        }
    }

    @Test
    fun `GET pair returns pair as JSON array`() {
        val req = Request.Builder()
            .url("$baseUrl/pair")
            .build()
        client.newCall(req).execute().use { resp ->
            assertEquals(200, resp.code)
            assertTrue(resp.body.string().contains("um"))
        }
    }

    @Test
    fun `GET path with path variable returns correct string`() {
        val req = Request.Builder()
            .url("$baseUrl/path/hello")
            .build()
        client.newCall(req).execute().use { resp ->
            assertEquals(200, resp.code)
            assertEquals("\"hello!\"", resp.body.string())
        }
    }

    @Test
    fun `GET args with query params returns correct map`() {
        val url = HttpUrl.Builder()
            .scheme("http")
            .host("localhost")
            .port(port)
            .addPathSegments("api/args")
            .addQueryParameter("n", "2")
            .addQueryParameter("text", "hi")
            .build()
        val req = Request.Builder().url(url).build()
        client.newCall(req).execute().use { resp ->
            assertEquals(200, resp.code)
            assertTrue(resp.body.string().contains("hihi"))
        }
    }
}
