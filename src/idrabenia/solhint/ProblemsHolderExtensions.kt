package idrabenia.solhint

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement


fun ProblemsHolder.addProblem(elem: PsiElement, text: String): ProblemsHolder {
    this.registerProblem(elem, text)
    return this
}