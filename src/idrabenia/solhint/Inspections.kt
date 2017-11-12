package idrabenia.solhint

import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiFile
import java.io.File
import java.util.concurrent.TimeUnit


abstract class Inspections : LocalInspectionTool() {
    override fun getDisplayName() = "Solhint Errors"

    override fun checkFile(file: PsiFile, manager: InspectionManager, isOnTheFly: Boolean) =
        runSolhint(file, manager)
            .filter { errorRegex().matches(it) }
            .map { errorRegex().find(it)?.groupValues }
            .map { Error(it!!) }
            .filter { it.severityUpper() == level() }
            .foldRight(ProblemsHolder(manager, file, false)) { error, holder ->
                holder.addProblem(error.targetElem(file), error.text)
            }
            .resultsArray

    private fun runSolhint(file: PsiFile, manager: InspectionManager) =
        ProcessBuilder()
            .directory(File(manager.project.baseDir.path))
            .command("node", solhintPath(), file.virtualFile.path)
            .start()
            .waitUntilExit(10, TimeUnit.SECONDS)
            .stdout()

    private fun solhintPath() = exec("which solhint").stdout()[0]

    private fun errorRegex() = Regex(
        ".+?([0-9]+):([0-9]+).+?(error|warning)[\\ ]+([A-Za-z0-9]+[A-Za-z0-9\\ \\.]*[A-Za-z0-9]+)[\\ ]+([a-z0-9\\-]*)$"
    )

    private fun exec(arg: String) = Runtime.getRuntime().exec(arg)

    protected abstract fun level(): String
}


class ErrorInspections : Inspections() {
    override fun level() = "ERROR"
}


class WarnInspections : Inspections() {
    override fun level() = "WARNING"
}
