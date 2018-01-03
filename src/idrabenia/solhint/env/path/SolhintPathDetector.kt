package idrabenia.solhint.env.path

import java.io.File
import java.util.*


object SolhintPathDetector : BasePathDetector() {

    private val solhintName = "solhint"

    fun detectSolhintPath(nodePath: String) =
        solhintForNode(nodePath) ?: firstSolhintFromEnv() ?: ""

    fun solhintForNode(nodePath: String) =
        optional(solhintCmdThatSiblingToNode(nodePath))
            .map { value -> solhintJsPath(value) }
            .orElse(null)

    fun firstSolhintFromEnv() =
        detectAllSolhintPaths()
            .firstOrNull()

    fun detectAllSolhintPaths() =
        detectAllInPaths(solhintName)
            .map { solhintJsPath(it) }
            .filterNotNull()

    private fun solhintCmdThatSiblingToNode(nodePath: String) =
        detectWithFilter(solhintName, { it.parent == File(nodePath).parent })

    private fun solhintJsPath(cmdPath: String): String? {
        val cmdRealPath = resolveSymlink(cmdPath)

        if (cmdRealPath.endsWith("solhint.js")) {
            return cmdRealPath
        } else {
            return solhintJsPathOnWindows(cmdPath)
        }
    }

    private fun solhintJsPathOnWindows(cmdPath: String) =
        optional(solhintJsFileOnWin(cmdPath))
            .filter(File::exists)
            .map(File::getAbsolutePath)
            .orElse(null)

    private fun solhintJsFileOnWin(solhintCmd: String) =
        optional(resolveSymlink(solhintCmd))
            .map(::File)
            .map { it.resolveSibling("node_modules/solhint/solhint.js") }
            .orElse(null)

    private fun resolveSymlink(filePath: String) =
        File(filePath)
            .toPath()
            .toRealPath()
            .toFile()
            .absolutePath

    private fun <T> optional(value: T?) =
        Optional.ofNullable<T>(value)

}
