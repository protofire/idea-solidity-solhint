package idrabenia.solhint.settings.data

import com.intellij.openapi.application.ApplicationManager.getApplication
import java.io.File


object SettingsManager {

    fun nodePath() =
        state().nodePath

    fun setNodePath(newPath: String) {
        repo().loadState(Settings(newPath))
    }

    fun solhintPath() =
        File(nodePath())
            .resolveSibling("solhint")
            .absolutePath

    private fun state() =
        repo().state

    private fun repo() =
        getApplication()?.getComponent(SettingsRepo::class.java) ?: SettingsRepo()

}
