package idrabenia.solhint.env.path

import com.intellij.execution.configurations.PathEnvironmentVariableUtil.findAllExeFilesInPath
import com.intellij.execution.configurations.PathEnvironmentVariableUtil.findInPath
import java.lang.System.getProperty


object NodePathDetector : PathDetector() {

    fun detectNodePath() =
         nodeWithSolhint() ?: detectPath(nodeBinName()) ?: "node"

    fun detectAllNodePaths() =
        detectAllInPaths(nodeBinName())

    fun nodeWithSolhint() =
        detectWithFilter(nodeBinName(), { it.resolveSibling("solhint").exists() })

    fun nodeBinName() =
        if (getProperty("os.name").contains("windows", true)) {
            "node.exe"
        } else {
            "node"
        }

}
