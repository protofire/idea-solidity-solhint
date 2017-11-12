package idrabenia.solhint

import com.google.gson.Gson
import org.apache.commons.compress.utils.IOUtils
import org.apache.commons.compress.utils.IOUtils.toByteArray
import java.io.File
import java.io.InputStream
import java.lang.ProcessBuilder.Redirect.from
import java.net.URL
import java.nio.charset.Charset.forName
import java.util.*


class SolhintClient () {
    val gson = Gson()
    var server: SolhintServer? = null

    fun startServer(projectDir: String): SolhintClient =
        if (server?.isAlive() as Boolean) {
            server = SolhintServer(projectDir)
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


class SolhintServer(val baseDir: String) {
    val process = start()

    fun start(): Process =
        ProcessBuilder()
            .redirectInput(from(serverCodeFile()))
            .directory(File(baseDir))
            .command("node")
            .start()

    fun stop(): Process =
        process.destroyForcibly()

    fun isAlive() =
        process.isAlive

    private fun serverCodeFile(): File =
        File
            .createTempFile("solhint-server", ".js")
            .writeFrom(javaClass.getResourceAsStream("solhint-server.js"))

    private fun File.writeFrom(inputStream: InputStream): File {
        IOUtils.copy(inputStream, this.outputStream())
        return this
    }
}


class ErrorList() : ArrayList<Error>()