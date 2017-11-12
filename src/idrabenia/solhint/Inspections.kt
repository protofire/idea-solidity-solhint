package idrabenia.solhint

import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiFile


abstract class Inspections : LocalInspectionTool() {
    private val solhintClient = SolhintClient()
    override fun getDisplayName() = "Solhint Errors"

    override fun checkFile(file: PsiFile, manager: InspectionManager, isOnTheFly: Boolean) =
        runSolhint(file, manager)
            .filter { it.severityAsText() == level() }
            .foldRight(ProblemsHolder(manager, file, false)) { error, holder ->
                holder.addProblem(error.targetElem(file), error.message)
            }
            .resultsArray

    private fun runSolhint(file: PsiFile, manager: InspectionManager) =
        solhintClient.fileErrors(manager.project.baseDir.path, file.virtualFile.path)

    protected abstract fun level(): String
}


class ErrorInspections : Inspections() {
    override fun level() = "ERROR"
}


class WarnInspections : Inspections() {
    override fun level() = "WARNING"
}
