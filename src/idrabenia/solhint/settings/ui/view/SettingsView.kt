package idrabenia.solhint.settings.ui.view

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor
import com.intellij.ui.TextFieldWithHistoryWithBrowseButton
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridConstraints.*
import com.intellij.uiDesigner.core.GridLayoutManager
import com.intellij.uiDesigner.core.Spacer
import com.intellij.util.ui.SwingHelper.addHistoryOnExpansion
import com.intellij.util.ui.SwingHelper.installFileCompletionAndBrowseDialog
import idrabenia.solhint.client.PathExecutableDetector
import idrabenia.solhint.common.Debouncer
import idrabenia.solhint.settings.ui.view.MessagePanel
import java.awt.EventQueue
import java.awt.Insets
import java.util.concurrent.TimeUnit.MILLISECONDS
import java.util.function.Consumer
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener


class SettingsView(
        val nodePathVal: String,
        val nodePathListener: Consumer<String>,
        val installSolhintButtonListener: Runnable) {
    val messagePanel = MessagePanel(installSolhintButtonListener)
    val panel: JPanel = mainPanel()
    val nodeInterpreterField = findNodeFieldOf(panel)

    var nodePath: String
        get() =
            nodeInterpreterField.text

        set(value) {
            nodeInterpreterField.text = value
        }

    private fun mainPanel(): JPanel {
        val panel = JPanel()

        panel.setLayout(GridLayoutManager(2, 2, Insets(0, 0, 0, 0), -1, -1))

        panel.add(Spacer(), GridConstraints(1, 1, 1, 1, ANCHOR_CENTER, FILL_VERTICAL, 1, SIZEPOLICY_WANT_GROW,
                null, null, null, 0, false))

        panel.add(nodeFieldLabel(), GridConstraints(0, 0, 1, 1, ANCHOR_EAST, FILL_NONE,
                SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false))

        panel.add(makeNodeInterpreterField(), GridConstraints(0, 1, 1, 1, ANCHOR_CENTER, FILL_HORIZONTAL,
                SIZEPOLICY_WANT_GROW, SIZEPOLICY_FIXED, null, null, null, 0, false))

        panel.add(messagePanel.panel, GridConstraints(1, 1, 1, 1, ANCHOR_CENTER, FILL_BOTH,
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

        addHistoryOnExpansion(field.childComponent, { PathExecutableDetector.detectAllNodePaths() })

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

    private fun onNodePathChanged() {
        Debouncer.debounce("nodePathInput", processNodePathChanged(), 300, MILLISECONDS)
    }

    private fun processNodePathChanged() = Runnable {
        EventQueue.invokeLater {
            nodePathListener.accept(nodeInterpreterField.text)
        }
    }

    private fun findByName(component: JComponent, name: String) =
        component.components.find { it.name == name }

    private fun findNodeFieldOf(panel: JPanel) =
        findByName(panel, "nodeInterpreterField") as TextFieldWithHistoryWithBrowseButton
}
