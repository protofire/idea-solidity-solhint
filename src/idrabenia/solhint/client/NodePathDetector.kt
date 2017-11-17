package idrabenia.solhint.client

import com.intellij.execution.configurations.PathEnvironmentVariableUtil.findAllExeFilesInPath
import com.intellij.execution.configurations.PathEnvironmentVariableUtil.findInPath


object NodePathDetector {

    fun detectNodePath() =
         nodeWithSolhint() ?: findInPath("node")?.absolutePath ?: "node"

    fun detectAllNodePaths() =
        findAllExeFilesInPath("node").map { it.absolutePath }

    fun nodeWithSolhint() =
        findAllExeFilesInPath("node")
            .filter { it.resolveSibling("solhint").exists() }
            .firstOrNull()
            ?.absolutePath

}

