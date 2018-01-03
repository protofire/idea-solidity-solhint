package idrabenia.solhint.env.path

import java.io.File


class SolhintCmd(val cmdPath: String?) {

    fun pathToSolhintJs() =
        if (cmdPath != null) {
            solhintJs().path()
        } else {
            null
        }

    private fun solhintJs() =
        if (isWindows()) {
            WindowsSolhintJs(cmdPath!!)
        } else {
            DefaultSolhintJs(cmdPath!!)
        }

    private fun isWindows() =
        System.getProperty("os.name").contains("windows", true)

    abstract class BaseSolhintJs(val cmdPath: String) {
        abstract fun path(): String?

        protected fun cmdRealPath() =
            resolveSymlink(cmdPath)

        private fun resolveSymlink(filePath: String) =
            File(filePath).toPath().toRealPath().toFile().absolutePath
    }

    class DefaultSolhintJs(cmdPath: String) : BaseSolhintJs(cmdPath) {
        override fun path() =
            if (cmdRealPath().endsWith("solhint.js")) {
                cmdRealPath()
            } else {
                null
            }
    }

    class WindowsSolhintJs(cmdPath: String) : BaseSolhintJs(cmdPath) {
        override fun path() =
            if (filePath().exists()) {
                filePath().absolutePath
            } else {
                null
            }

        private fun filePath() =
            File(cmdRealPath()).resolveSibling("node_modules/solhint/solhint.js")
    }

}
