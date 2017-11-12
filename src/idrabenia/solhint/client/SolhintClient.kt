package idrabenia.solhint.client

import com.google.gson.Gson
import idrabenia.solhint.errors.ErrorList
import org.apache.commons.compress.utils.IOUtils.toByteArray
import java.net.URL
import java.nio.charset.Charset.forName


class SolhintClient () {
    val gson = Gson()
    var server: SolhintProcess? = null

    fun startServer(projectDir: String): SolhintClient =
        if (server == null || !server!!.isAlive()) {
            server = SolhintProcess(projectDir)
            this
        } else {
            this
        }

    fun fileErrors(projectDir: String, filePath: String): ErrorList =
        startServer(projectDir)
            .verifyFile(filePath)
            .toErrorList()

    fun verifyFile(filePath: String) =
        URL("http", "127.0.0.1", 3476, filePath)
            .openConnection()
            .inputStream
            .use {
                String(toByteArray(it), forName("utf-8"))
            }

    fun stopServer() =
        server?.stop()

    fun String.toErrorList(): ErrorList =
        gson.fromJson<ErrorList>(this, ErrorList::class.java)
}
