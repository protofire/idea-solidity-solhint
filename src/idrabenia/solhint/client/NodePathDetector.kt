package idrabenia.solhint.client

import com.intellij.execution.configurations.PathEnvironmentVariableUtil.findAllExeFilesInPath
import com.intellij.execution.configurations.PathEnvironmentVariableUtil.findInPath
import java.lang.System.getProperty


object NodePathDetector {

    fun detectNodePath() =
         nodeWithSolhint() ?: findInPath(nodeBinName())?.absolutePath ?: "node"

    fun detectAllNodePaths() =
        findAllExeFilesInPath(nodeBinName()).map { it.absolutePath }

    fun nodeWithSolhint() =
        findAllExeFilesInPath(nodeBinName())
            .filter { it.resolveSibling("solhint").exists() }
            .firstOrNull()
            ?.absolutePath

    fun nodeBinName() =
        if (getProperty("os.name").contains("windows", true)) {
            "node.exe"
        } else {
            "node"
        }

}
