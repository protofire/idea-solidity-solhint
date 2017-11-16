package idrabenia.solhint.settings.data

import com.intellij.openapi.application.ApplicationManager.getApplication
import idrabenia.solhint.client.Environment


object SettingsManager {

    fun nodePath() =
        state().nodePath

    fun setNodePath(newPath: String) {
        repo().loadState(Settings(newPath))
    }

    fun solhintPath() =
        nodePath().replace("node$", "solhint")

    private fun state() =
        repo().state

    private fun repo() =
        getApplication()?.getComponent(SettingsRepo::class.java) ?: SettingsRepo()

}
