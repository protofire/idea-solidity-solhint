package idrabenia.solhint.client

import com.google.gson.Gson
import com.intellij.openapi.diagnostic.Logger
import idrabenia.solhint.client.process.AbstractSolhintProcess
import idrabenia.solhint.common.IoStreams.toByteArray
import idrabenia.solhint.env.Environment
import idrabenia.solhint.errors.ErrorList
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.Charset.forName


object SolhintClient {
    val LOG = Logger.getInstance(SolhintClient::class.java)

    val gson = Gson()
    var server: AbstractSolhintProcess? = null

    fun startServer(projectDir: String): SolhintClient =
        if (server == null || !server!!.isAlive()) {
            server = Environment.solhintServer(projectDir)
            this
        } else {
            this
        }

    fun fileErrors(projectDir: String, filePath: String): ErrorList =
        startServer(projectDir)
            .verifyFile(filePath)
            .toErrorList()

    fun verifyFile(filePath: String) =
        try {
            URL("http", "127.0.0.1", server!!.port, "?filePath=${urlEncode(filePath)}")
                .openConnection()
                .inputStream
                .use {
                    String(toByteArray(it), forName("utf-8"))
                }
        } catch (e: Exception) {
            LOG.warn("Can not connect to Solhint validation server", e)

            "[]"
        }

    fun stopServer() =
        server?.stop()

    fun String.toErrorList(): ErrorList =
        gson.fromJson<ErrorList>(this, ErrorList::class.java)

    private fun urlEncode(value: String) =
        URLEncoder.encode(value, "utf-8")
}
