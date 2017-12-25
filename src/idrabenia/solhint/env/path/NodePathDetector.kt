package idrabenia.solhint.env.path

import java.lang.System.getProperty


object NodePathDetector : BasePathDetector() {

    private val nodeFileName: String
        get() = if (isWindows()) "node.exe" else "node"

    fun detectNodePath() =
         nodeWithSolhint() ?: detectPath(nodeFileName) ?: "node"

    fun detectAllNodePaths() =
        detectAllInPaths(nodeFileName)

    fun nodeWithSolhint() =
        detectWithFilter(nodeFileName) { it.resolveSibling("solhint").exists() }

    private fun isWindows() =
        getProperty("os.name").contains("windows", true)

}
