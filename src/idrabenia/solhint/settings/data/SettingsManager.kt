package idrabenia.solhint.settings.data

import com.intellij.openapi.application.ApplicationManager.getApplication


object SettingsManager {

    fun nodePath() =
        state().nodePath

    fun solhintPath() =
        state().solhintPath

    fun setNodePath(newPath: String) {
        repo().loadState(Settings(newPath, solhintPath()))
    }

    fun setSolhintPath(newPath: String) {
        repo().loadState(Settings(nodePath(), newPath));
    }

    private fun state() =
        repo().state

    private fun repo() =
        getApplication()?.getComponent(SettingsRepo::class.java) ?: SettingsRepo()

}
