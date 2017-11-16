package idrabenia.solhint.settings.data

import com.intellij.openapi.application.ApplicationManager
import idrabenia.solhint.client.Environment


object SettingsManager {

    fun nodePath() =
        state().nodePath

    fun setNodePath(newPath: String) {
        repo().loadState(Settings(newPath))
        Environment.validateDependencies()
    }

    private fun state() =
        repo().state

    private fun repo() =
        ApplicationManager
            .getApplication()
            .getComponent(SettingsRepo::class.java)

}
