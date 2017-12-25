package idrabenia.solhint.client.path

import com.intellij.execution.configurations.PathEnvironmentVariableUtil.findAllExeFilesInPath
import com.intellij.execution.configurations.PathEnvironmentVariableUtil.findInPath
import java.io.File
import java.lang.System.getProperty


object SolhintPathDetector {

    fun detectSolhintPath(nodePath: String) =
        solhintForNode(nodePath) ?: findInPath(solhintName())?.absolutePath ?: ""

    fun detectAllSolhintPaths() =
        findAllExeFilesInPath(solhintName()).map { it.absolutePath }

    fun solhintForNode(nodePath: String) =
        findAllExeFilesInPath(solhintName())
            .filter { it.startsWith(File(nodePath).parent) }
            .firstOrNull()
            ?.absolutePath

    fun solhintName() =
        "solhint"

}
