package idrabenia.solhint

import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiUtil


class Error (val line: Int, val column: Int, val severity: String, val text: String) {

    constructor(items: List<String>):
            this(Integer.parseInt(items[1]), Integer.parseInt(items[2]), items[3], items[4])

    fun offsetIn(file: PsiFile) =
            lineOffset(file) + (column - 1)

    fun lineOffset(file: PsiFile) = file
            .viewProvider
            .document!!
            .getLineStartOffset(line - 1)

    fun targetElem(file: PsiFile) =
            PsiUtil.getElementAtOffset(file, offsetIn(file))

    fun severityUpper() =
            this.severity.toUpperCase()

}