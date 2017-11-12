package idrabenia.solhint

import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiUtil


class Error (val line: Int, val column: Int, val severity: Int, val message: String) {

    fun offsetIn(file: PsiFile) =
            lineOffset(file) + (column - 1)

    fun lineOffset(file: PsiFile) =
        try {
            document(file).getLineStartOffset(line - 1)
        } catch (ex: Exception) {
            document(file).lineCount
        }

    fun targetElem(file: PsiFile) =
            PsiUtil.getElementAtOffset(file, offsetIn(file))

    fun severityAsText() =
        if (this.severity == 3) {
            "WARNING"
        } else {
            "ERROR"
        }

    private fun document(file: PsiFile) =
        file
            .viewProvider
            .document!!
}