package idrabenia.solhint.env.path

import java.io.File


object SolhintPathDetector : BasePathDetector() {

    private val solhintName = "solhint"

    fun detectSolhintPath(nodePath: String) =
        solhintForNode(nodePath) ?: detectPath(solhintName) ?: ""

    fun detectAllSolhintPaths() =
        detectAllInPaths(solhintName)
            .map { it.toSolhintJsPath() }
            .filter { it != null && !it.isEmpty() }

    fun solhintForNode(nodePath: String) =
        detectWithFilter(solhintName)
            { it.startsWith(File(nodePath).parent) }
            ?.toSolhintJsPath()

    private fun String.toSolhintJsPath() =
        if (realPath(this).endsWith("solhint.js")) {
            realPath(this)
        } else if (solhintJsWinFile(this).exists()) {
            solhintJsWinFile(this).absolutePath
        } else {
            null
        }

    private fun solhintJsWinFile(solhintPath: String) =
        File(realPath(solhintPath)).resolveSibling("node_modules/solhint/solhint.js")

    private fun realPath(filePath: String) =
        File(filePath)
            .toPath()
            .toRealPath()
            .toFile()
            .absolutePath

}
