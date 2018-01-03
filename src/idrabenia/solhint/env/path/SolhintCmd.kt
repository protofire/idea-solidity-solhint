package idrabenia.solhint.env.path

import java.io.File


class SolhintCmd(val cmdPath: String?) {

    constructor(init: () -> String?): this(init())

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

}

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
        solhintJsPathOnWindows()

    private fun solhintJsPathOnWindows() =
        if (solhintJsFileOnWin().exists()) {
            solhintJsFileOnWin().absolutePath
        } else {
            null
        }

    private fun solhintJsFileOnWin() =
        File(cmdRealPath()).resolveSibling("node_modules/solhint/solhint.js")
}
