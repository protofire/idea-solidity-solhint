package idrabenia.solhint.settings.ui.view

import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridConstraints.*
import com.intellij.uiDesigner.core.GridLayoutManager
import com.intellij.uiDesigner.core.Spacer
import idrabenia.solhint.settings.ui.view.MessagePanel.State.*
import java.awt.Color
import java.awt.Font
import java.awt.Insets
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel


class MessagePanel(val installSolhintButtonListener: () -> Unit) {
    enum class State {
        HIDE_ALL, INCORRECT, SOLHINT_INCORRECT, INSTALL_REQUIRED, INSTALL_IN_PROGRESS, READY_TO_WORK
    }

    val panel = mainPanel()

    val pathIncorrectMessage = findByName(panel, "pathIncorrectMessage") as JLabel
    val installSolhintMessage = findByName(panel, "installSolhintMessage") as JPanel
    val solhintInstallInProgressMessage = findByName(panel, "solhintInstallInProgressMessage") as JLabel
    val readyMessage = findByName(panel, "readMessage") as JLabel
    val solhintPathIncorrectMessage = findByName(panel, "solhintPathIncorrectMessage") as JLabel

    fun setState(state: State) {
        hideAllMessages()

        val component: JComponent = when (state) {
            INCORRECT -> pathIncorrectMessage
            INSTALL_REQUIRED -> installSolhintMessage
            INSTALL_IN_PROGRESS -> solhintInstallInProgressMessage
            READY_TO_WORK -> readyMessage
            SOLHINT_INCORRECT -> solhintPathIncorrectMessage
            else -> JPanel()
        }

        component.isVisible = true
    }

    private fun mainPanel(): JPanel {
        val panel = JPanel()

        panel.layout = GridLayoutManager(6, 1, Insets(0, 0, 0, 0), -1, -1)

        panel.add(Spacer(), GridConstraints(4, 0, 1, 1, ANCHOR_CENTER, FILL_VERTICAL, 1, SIZEPOLICY_WANT_GROW,
                null, null, null, 0, false))

        panel.add(pathIncorrectLabel(), GridConstraints(0, 0, 1, 1, ANCHOR_WEST, FILL_NONE, SIZEPOLICY_FIXED,
                SIZEPOLICY_FIXED, null, null, null, 0, false))

        panel.add(installSolhintPanel(), GridConstraints(1, 0, 1, 1, ANCHOR_CENTER, FILL_BOTH,
                SIZEPOLICY_CAN_SHRINK or SIZEPOLICY_CAN_GROW, SIZEPOLICY_CAN_SHRINK or SIZEPOLICY_CAN_GROW,
                null, null, null, 0, false))

        panel.add(solhintInstallationMessage(), GridConstraints(2, 0, 1, 1, ANCHOR_WEST, FILL_NONE,
                SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false))

        panel.add(readyToWorkMessage(), GridConstraints(3, 0, 1, 1, ANCHOR_WEST, FILL_NONE, SIZEPOLICY_FIXED,
                SIZEPOLICY_FIXED, null, null, null, 0, false))

        panel.add(solhintPathIncorrectLabel(), GridConstraints(4, 0, 1, 1, ANCHOR_WEST, FILL_NONE, SIZEPOLICY_FIXED,
                SIZEPOLICY_FIXED, null, null, null, 0, false))

        return panel
    }

    private fun pathIncorrectLabel(): JLabel {
        val label = JLabel()

        label.name = "pathIncorrectMessage"
        label.font = Font(label.getFont().getName(), Font.ITALIC, label.getFont().getSize())
        label.foreground = Color(-1623760)
        label.text = "Path to Node.js binary is incorrect"
        label.isVisible = false

        return label
    }

    private fun solhintPathIncorrectLabel(): JLabel {
        val label = JLabel()

        label.name = "solhintPathIncorrectMessage"
        label.font = Font(label.getFont().getName(), Font.ITALIC, label.getFont().getSize())
        label.foreground = Color(-1623760)
        label.text = "Solhint path is incorrect. You need target file `node_modules/solhint/solhint.js`"
        label.isVisible = false

        return label
    }

    private fun installSolhintPanel(): JPanel {
        val panel = JPanel()

        panel.name = "installSolhintMessage"
        panel.isVisible = false
        panel.layout = GridLayoutManager(1, 2, Insets(0, 0, 0, 0), -1, -1)

        val label = JLabel()
        label.font = Font(label.font.name, Font.ITALIC, label.font.size)
        label.foreground = Color(-1623760)
        label.text = "Solhint package is not found"
        panel.add(label, GridConstraints(0, 0, 1, 1, ANCHOR_WEST, FILL_NONE, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED,
                null, null, null, 0, false))

        val installSolhintButton = JButton()
        installSolhintButton.text = "Install Solhint"
        installSolhintButton.addActionListener { installSolhintButtonListener.invoke() }
        panel.add(installSolhintButton, GridConstraints(0, 1, 1, 1, ANCHOR_WEST, FILL_NONE,
                SIZEPOLICY_CAN_SHRINK or SIZEPOLICY_CAN_GROW, SIZEPOLICY_FIXED, null, null, null, 0, false))

        return panel
    }

    private fun solhintInstallationMessage(): JLabel {
        val label = JLabel()

        label.name = "solhintInstallInProgressMessage"
        label.font = Font(label.font.name, Font.ITALIC, label.font.size)
        label.foreground = Color(-12543001)
        label.text = "Installing Solhint. Please Wait..."
        label.isVisible = false

        return label
    }

    private fun readyToWorkMessage(): JLabel {
        val label = JLabel()

        label.name = "readMessage"
        label.font = Font(label.font.name, Font.BOLD, label.font.size)
        label.foreground = Color(-11940505)
        label.text = "Solhint plugin ready to work!"
        label.isVisible = false

        return label
    }

    private fun hideAllMessages() {
        pathIncorrectMessage.isVisible = false
        installSolhintMessage.isVisible = false
        solhintInstallInProgressMessage.isVisible = false
        solhintPathIncorrectMessage.isVisible = false
        readyMessage.isVisible = false
    }

    private fun findByName(component: JComponent, name: String) =
        component.components.find { it.name == name }

}
