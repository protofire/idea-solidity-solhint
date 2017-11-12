package idrabenia.solhint

import com.google.gson.Gson
import jdk.nashorn.internal.parser.JSONParser
import org.apache.commons.compress.utils.IOUtils
import org.apache.commons.compress.utils.IOUtils.toByteArray
import java.io.File
import java.net.URL
import java.nio.charset.Charset.forName
import java.util.*


class SolhintClient () {
    val gson = Gson()
    var server: Server? = null

    fun startServer(projectDir: String) {
        if (server == null || !server!!.isAlive()) {
            server = Server(projectDir)
        }
    }

    fun fileErrors(projectDir: String, filePath: String): ErrorList {
        startServer(projectDir)

        return gson
            .fromJson<ErrorList>(
                verifyFile(filePath),
                ErrorList::class.java
            )
    }

    fun verifyFile(filePath: String) =
        URL("http", "127.0.0.1", 3476, filePath)
            .openConnection()
            .inputStream
            .use {
                String(toByteArray(it), forName("utf-8"))
            }

    fun stopServer() {
        if (server != null) {
            server!!.stop()
            server = null
        }
    }
}


class Server (val baseDir: String) {
    val process = start()

    fun start() =
        ProcessBuilder()
            .redirectInput(ProcessBuilder.Redirect.from(solhintServerCode()))
            .directory(File(baseDir))
            .command("node")
            .start()

    fun stop() =
            process.destroyForcibly()

    fun isAlive() =
        process.isAlive

    private fun solhintServerCode(): File {
        val file = File.createTempFile("solhint-server", ".js")
        IOUtils.copy(javaClass.getResourceAsStream("solhint-server.js"), file.outputStream())
        return file
    }
}


class ErrorList() : ArrayList<Error>() {

}