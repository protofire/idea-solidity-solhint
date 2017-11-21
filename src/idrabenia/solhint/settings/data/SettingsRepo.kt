package idrabenia.solhint.settings.data

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import idrabenia.solhint.client.PathExecutableDetector.detectNodePath


@State(
    name = "SolhintProjectComponent",
    storages = arrayOf(Storage(file = "solhint.xml"))
)
class SettingsRepo() : PersistentStateComponent<Settings> {
    private var settings = Settings(detectNodePath())

    override fun getState() = settings

    override fun loadState(state: Settings) {
        this.settings = state
    }
}
