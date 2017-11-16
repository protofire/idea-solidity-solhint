package idrabenia.solhint.settings.data

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import idrabenia.solhint.utils.IoStreams
import java.nio.charset.Charset.forName


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

    private fun detectNodePath() =
        try {
            if (System.getProperty("os.name").contains("windows", true)) {
                exec("where node")
            } else {
                exec("which node")
            }
        } catch (e: Exception) {
            "node"
        }

    private fun exec(cmd: String): String {
        val process = Runtime.getRuntime().exec(cmd)
        val bytes = IoStreams.toByteArray(process.inputStream)
        val results = String(bytes, forName("utf-8"))

        return results.trim()
    }
}
