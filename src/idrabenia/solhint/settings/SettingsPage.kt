package idrabenia.solhint.settings

import com.intellij.openapi.options.Configurable
import idrabenia.solhint.client.Environment
import idrabenia.solhint.client.SolhintClient
import idrabenia.solhint.settings.data.SettingsManager
import idrabenia.solhint.settings.data.SettingsManager.nodePath
import javax.swing.JComponent


class SettingsPage : Configurable {
    val view = SettingsView(nodePath())

    override fun getDisplayName() = "Solidity Solhint Settings"

    override fun getHelpTopic() = "There is page that allow to configure Solidity Solhint Plugin"

    override fun isModified(): Boolean {
        return nodePath() != view.nodePath
    }

    override fun apply() {
        SettingsManager.setNodePath(view.nodePath)

        Environment.validateDependencies()
        SolhintClient.stopServer()
    }

    override fun createComponent(): JComponent? {
        return view.panel
    }

    override fun reset() {
        view.nodePath = nodePath()
    }

    override fun disposeUIResources() {
        // noop
    }
}
