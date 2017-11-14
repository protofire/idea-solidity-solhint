package idrabenia.solhint.client.process

import idrabenia.solhint.utils.IoStreams.copy
import java.io.File
import java.io.InputStream
import java.util.*


class ServerProcess(val baseDir: String) : AbstractSolhintProcess {
    override val port = 55000 + Random().nextInt(1000)
    override val process = start()

    fun start(): Process? =
        ProcessBuilder()
            .directory(File(baseDir))
            .command("node", serverCodeFile().absolutePath, port.toString())
            .start()
            .killOnShutdown()

    override fun stop() {
        process!!.destroyForcibly()
    }

    override fun isAlive() =
        process != null && process.isAlive

    private fun serverCodeFile(): File =
        File
            .createTempFile("solhint-server", ".js")
            .writeFrom(javaClass.getResourceAsStream("solhint-server.js"))

    private fun File.writeFrom(inputStream: InputStream): File {
        copy(inputStream, this.outputStream())
        return this
    }

    private fun Process.killOnShutdown(): Process {
        Runtime.getRuntime().addShutdownHook(Thread({ this.destroyForcibly() }))
        return this
    }
}
