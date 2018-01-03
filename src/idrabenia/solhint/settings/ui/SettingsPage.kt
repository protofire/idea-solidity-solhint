package idrabenia.solhint.settings.ui

import com.intellij.openapi.application.ApplicationManager.getApplication
import com.intellij.openapi.options.Configurable
import idrabenia.solhint.client.SolhintClient
import idrabenia.solhint.env.Environment
import idrabenia.solhint.env.Environment.isCorrectSolhintPath
import idrabenia.solhint.env.Environment.isSolhintInstalledInNode
import idrabenia.solhint.env.Environment.solhintNodeRelativePath
import idrabenia.solhint.env.path.SolhintPathDetector.detectAllSolhintPaths
import idrabenia.solhint.settings.data.SettingsManager
import idrabenia.solhint.settings.data.SettingsManager.nodePath
import idrabenia.solhint.settings.data.SettingsManager.solhintPath
import idrabenia.solhint.settings.ui.view.MessagePanel
import idrabenia.solhint.settings.ui.view.MessagePanel.State.*
import idrabenia.solhint.settings.ui.view.SettingsView
import java.awt.EventQueue
import javax.swing.JComponent


class SettingsPage : Configurable {
    val view = SettingsView(
        nodePath(),
        solhintPath(),
        { onNodePathChanged(it) },
        { onSolhintPathChanged(it) },
        { installSolhint() }
    )

    override fun getDisplayName() = "Solidity Solhint Settings"

    override fun getHelpTopic() = "There is page that allow to configure Solidity Solhint Plugin"

    override fun isModified(): Boolean =
        nodePath() != view.nodePath || solhintPath() != view.solhintPath

    override fun apply() {
        SettingsManager.setNodePath(view.nodePath)
        SettingsManager.setSolhintPath(view.solhintPath)

        Environment.validateDependencies()
        SolhintClient.stopServer()
    }

    override fun createComponent(): JComponent? {
        onNodePathChanged(view.nodePath)

        return view.panel
    }

    override fun reset() {
        view.nodePath = nodePath()
        view.solhintPath = solhintPath()
    }

    fun onNodePathChanged(newNodePath: String) {
        if (isSolhintInstalledInNode(newNodePath)) {
            view.solhintPath = solhintNodeRelativePath(newNodePath).absolutePath
        }

        validateSolhintPath(view.solhintPath)
        validateNodePath(newNodePath)
    }

    fun onSolhintPathChanged(newSolhintPath: String): Unit =
        validateSolhintPath(newSolhintPath)

    private fun validateSolhintPath(solhintPath: String) =
        if (detectAllSolhintPaths().isEmpty()) {
            setStatus(INSTALL_REQUIRED)
        } else if (isCorrectSolhintPath(solhintPath)) {
            setStatus(READY_TO_WORK)
        } else {
            setStatus(SOLHINT_INCORRECT)
        }

    private fun validateNodePath(nodePath: String) =
        if (!Environment.isNodeJsInstalled(nodePath)) {
            setStatus(INCORRECT)
        } else {
            setStatus(READY_TO_WORK)
        }

    private fun setStatus(status: MessagePanel.State) =
        view.setMessage(status)

    fun installSolhint() {
        setStatus(INSTALL_IN_PROGRESS)

        getApplication().executeOnPooledThread {
            Environment.installSolhint(view.nodePath)

            EventQueue.invokeLater { onNodePathChanged(view.nodePath) }
        }
    }

    override fun disposeUIResources() {
        // noop
    }
}
