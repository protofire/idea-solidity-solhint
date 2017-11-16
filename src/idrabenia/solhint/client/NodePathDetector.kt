package idrabenia.solhint.client

import idrabenia.solhint.common.IoStreams
import java.nio.charset.Charset


object NodePathDetector {

    fun detectNodePath() =
        try {
            if (System.getProperty("os.name").contains("windows", true)) {
                exec("where node")
            } else {
                exec("which node")
            }
        } catch (e: Exception) {
            "node"
        }

    private fun exec(cmd: String): String {
        val process = Runtime.getRuntime().exec(cmd)
        val bytes = IoStreams.toByteArray(process.inputStream)
        val results = String(bytes, Charset.forName("utf-8"))

        return results.trim()
    }

}