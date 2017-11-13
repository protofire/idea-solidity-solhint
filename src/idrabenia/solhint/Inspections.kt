package idrabenia.solhint

import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import idrabenia.solhint.client.SolhintClient


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

    protected abstract fun level(): String

    private fun runSolhint(file: PsiFile, manager: InspectionManager) =
            solhintClient.fileErrors(manager.project.baseDir.path, file.virtualFile.path)

    private fun ProblemsHolder.addProblem(elem: PsiElement, text: String): ProblemsHolder {
        this.registerProblem(elem, text)
        return this
    }
}

class ErrorInspections : Inspections() {
    override fun level() = "ERROR"
}

class WarnInspections : Inspections() {
    override fun level() = "WARNING"
}
