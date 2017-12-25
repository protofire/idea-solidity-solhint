package idrabenia.solhint.env.path

import com.intellij.execution.configurations.PathEnvironmentVariableUtil.findAllExeFilesInPath
import com.intellij.execution.configurations.PathEnvironmentVariableUtil.findInPath
import java.io.File


open class BasePathDetector {

    fun detectPath(cmdName: String) =
        findInPath(cmdName)?.absolutePath

    fun detectAllInPaths(cmdName: String) =
        findAllExeFilesInPath(cmdName).map { it.absolutePath }

    fun detectWithFilter(cmdName: String, filter: (file: File) -> Boolean) =
        findAllExeFilesInPath(cmdName)
            .filter(filter)
            .firstOrNull()
            ?.absolutePath

}
