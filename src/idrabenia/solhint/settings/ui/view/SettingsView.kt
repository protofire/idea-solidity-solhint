package idrabenia.solhint.settings.ui.view

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor
import com.intellij.ui.TextFieldWithHistoryWithBrowseButton
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridConstraints.*
import com.intellij.uiDesigner.core.GridLayoutManager
import com.intellij.uiDesigner.core.Spacer
import com.intellij.util.ui.SwingHelper.addHistoryOnExpansion
import com.intellij.util.ui.SwingHelper.installFileCompletionAndBrowseDialog
import idrabenia.solhint.common.Debouncer
import idrabenia.solhint.env.path.NodePathDetector
import idrabenia.solhint.env.path.SolhintPathDetector
import java.awt.EventQueue
import java.awt.Insets
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener


class SettingsView(
        val nodePathVal: String,
        val solhintPathVal: String,
        val nodePathListener: (String) -> Unit,
        val solhintPathListener: (String) -> Unit,
        installSolhintButtonListener: () -> Unit) {
    val messagePanel = MessagePanel(installSolhintButtonListener)
    val panel: JPanel = mainPanel()
    val nodeInterpreterField = findNodeFieldOf(panel)
    val solhintPathField = findSolhintFieldOf(panel)

    var nodePath: String
        get() =
            nodeInterpreterField.text

        set(value) {
            nodeInterpreterField.text = value
        }

    var solhintPath: String
        get() =
            solhintPathField.text

        set(value) {
            solhintPathField.text = value
        }

    fun setMessage(state: MessagePanel.State) {
        messagePanel.setState(state)
    }

    private fun mainPanel(): JPanel {
        val panel = JPanel()

        panel.setLayout(GridLayoutManager(4, 2, Insets(0, 0, 0, 0), -1, -1))

        panel.add(Spacer(), GridConstraints(3, 1, 1, 1, ANCHOR_CENTER, FILL_VERTICAL, 1, SIZEPOLICY_WANT_GROW,
                null, null, null, 0, false))

        panel.add(nodeFieldLabel(), GridConstraints(0, 0, 1, 1, ANCHOR_EAST, FILL_NONE,
                SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false))

        panel.add(makeNodeInterpreterField(), GridConstraints(0, 1, 1, 1, ANCHOR_CENTER, FILL_HORIZONTAL,
                SIZEPOLICY_WANT_GROW, SIZEPOLICY_FIXED, null, null, null, 0, false))

        panel.add(solhintFieldLabel(), GridConstraints(1, 0, 1, 1, ANCHOR_EAST, FILL_NONE,
                SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false))

        panel.add(makeSolhintPathField(), GridConstraints(1, 1, 1, 1, ANCHOR_CENTER, FILL_HORIZONTAL,
                SIZEPOLICY_WANT_GROW, SIZEPOLICY_FIXED, null, null, null, 0, false))

        panel.add(messagePanel.panel, GridConstraints(2, 1, 1, 1, ANCHOR_CENTER, FILL_BOTH,
                SIZEPOLICY_CAN_SHRINK or SIZEPOLICY_CAN_GROW, SIZEPOLICY_CAN_SHRINK or SIZEPOLICY_CAN_GROW,
                null, null, null, 0, false))

        return panel
    }

    private fun makeNodeInterpreterField(): TextFieldWithHistoryWithBrowseButton {
        val field = TextFieldWithHistoryWithBrowseButton()
        field.name = "nodeInterpreterField"
        field.text = nodePathVal

        field.childComponent.addDocumentListener(object : DocumentListener {
            override fun changedUpdate(e: DocumentEvent?) = onNodePathChanged()
            override fun insertUpdate(e: DocumentEvent?) = onNodePathChanged()
            override fun removeUpdate(e: DocumentEvent?) = onNodePathChanged()
        })

        val caption = "Select Node.js Executable"
        installFileCompletionAndBrowseDialog(null, field, caption, createSingleFileNoJarsDescriptor())

        addHistoryOnExpansion(field.childComponent, { NodePathDetector.detectAllNodePaths() })

        return field
    }

    private fun nodeFieldLabel(): JLabel {
        val label = JLabel()

        label.text = "Node Binary"
        label.setDisplayedMnemonic('N')
        label.displayedMnemonicIndex = 0
        label.toolTipText = "This path should point to node interpeter file path."

        return label
    }

    private fun makeSolhintPathField(): TextFieldWithHistoryWithBrowseButton {
        val field = TextFieldWithHistoryWithBrowseButton()
        field.name = "solhintPathField"
        field.text = solhintPathVal

        field.childComponent.addDocumentListener(object : DocumentListener {
            override fun changedUpdate(e: DocumentEvent?) = onSolhintPathChanged()
            override fun insertUpdate(e: DocumentEvent?) = onSolhintPathChanged()
            override fun removeUpdate(e: DocumentEvent?) = onSolhintPathChanged()
        })

        val caption = "Select Solhint Executable"
        installFileCompletionAndBrowseDialog(null, field, caption, createSingleFileNoJarsDescriptor())

        addHistoryOnExpansion(field.childComponent, { SolhintPathDetector.detectAllSolhintPaths() })

        return field
    }

    private fun solhintFieldLabel(): JLabel {
        val label = JLabel()

        label.text = "Solhint Binary"
        label.setDisplayedMnemonic('S')
        label.displayedMnemonicIndex = 0
        label.toolTipText = "This path should point to Solhint executable file path."

        return label
    }

    private fun onNodePathChanged() {
        Debouncer.debounce("nodePathInput", processNodePathChanged(), 300, MILLISECONDS)
    }

    private fun processNodePathChanged() = Runnable {
        EventQueue.invokeLater {
            nodePathListener.invoke(nodeInterpreterField.text)
        }
    }

    private fun onSolhintPathChanged() {
        Debouncer.debounce("solhintPathInput", processSolhintPathChanged(), 300, MILLISECONDS)
    }

    private fun processSolhintPathChanged() = Runnable {
        EventQueue.invokeLater {
            solhintPathListener.invoke(solhintPathField.text)
        }
    }

    private fun findByName(component: JComponent, name: String) =
        component.components.find { it.name == name }

    private fun findNodeFieldOf(panel: JPanel) =
        findByName(panel, "nodeInterpreterField") as TextFieldWithHistoryWithBrowseButton

    private fun findSolhintFieldOf(panel: JPanel) =
        findByName(panel, "solhintPathField") as TextFieldWithHistoryWithBrowseButton
}
