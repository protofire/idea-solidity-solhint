package idrabenia.solhint.settings.data

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import idrabenia.solhint.client.path.NodePathDetector.detectNodePath
import idrabenia.solhint.client.path.SolhintPathDetector.detectSolhintPath


@State(
    name = "SolhintProjectComponent",
    storages = arrayOf(Storage(file = "solhint.xml"))
)
class SettingsRepo() : PersistentStateComponent<Settings> {
    private var settings = Settings(detectNodePath(), detectSolhintPath(detectNodePath()))

    override fun getState() = settings

    override fun loadState(state: Settings) {
        this.settings = state
    }
}
