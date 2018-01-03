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

    fun String.toSolhintJsPath() =
        if (this.realPath().endsWith("solhint.js")) {
            this.realPath()
        } else if (File(this.realPath()).resolve("node_modules/solhint/solhint.js").exists()){
            File(this.realPath())
                .resolve("node_modules/solhint/solhint.js")
                .absolutePath
        } else {
            null
        }

    fun String.realPath() =
        File(this)
            .toPath()
            .toRealPath()
            .toFile()
            .absolutePath

}
