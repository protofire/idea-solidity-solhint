package idrabenia.solhint.settings.ui

import com.intellij.openapi.application.ApplicationManager.getApplication
import com.intellij.openapi.options.Configurable
import idrabenia.solhint.client.Environment
import idrabenia.solhint.client.SolhintClient
import idrabenia.solhint.settings.data.SettingsManager
import idrabenia.solhint.settings.data.SettingsManager.nodePath
import idrabenia.solhint.settings.ui.view.MessagePanel.State.*
import idrabenia.solhint.settings.ui.view.SettingsView
import java.awt.EventQueue
import java.util.function.Consumer
import javax.swing.JComponent


class SettingsPage : Configurable {
    val view = SettingsView(nodePath(), Consumer<String> { onNodePathChanged(it) }, Runnable { installSolhint() })

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
        onNodePathChanged(view.nodePath)

        return view.panel
    }

    override fun reset() {
        view.nodePath = nodePath()
    }

    fun onNodePathChanged(value: String) {
        val messagePanel = view.messagePanel

        if (!Environment.isNodeJsInstalled(value)) {
            messagePanel.setState(INCORRECT)
            return
        }

        if (!Environment.isSolhintInstalledInNode(value)) {
            messagePanel.setState(INSTALL_REQUIRED)
            return
        }

        messagePanel.setState(READY_TO_WORK)
    }

    fun installSolhint() {
        view.messagePanel.setState(INSTALL_IN_PROGRESS)

        getApplication().executeOnPooledThread {
            Environment.installSolhint(view.nodePath)

            EventQueue.invokeLater { onNodePathChanged(view.nodePath) }
        }
    }

    override fun disposeUIResources() {
        // noop
    }
}
