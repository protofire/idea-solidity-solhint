package idrabenia.solhint.common

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType.ERROR
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager.getApplication
import com.intellij.openapi.options.ShowSettingsUtil
import idrabenia.solhint.settings.ui.SettingsPage


object IdeMessages {

    fun notifyThatNodeNotInstalled() =
        error(
            "Node.js is not found",
            "Please provide correct path to Node.js at Settings > Tools > Solhint page"
        )

    fun notifyThatSolhintNotInstalled() =
        error(
            "Solhint is not installed",
            "For correct run of Solidity Solhint Plugin you need to install Solhint. Just run 'npm install -g solhint'"
        )

    fun error(title: String, message: String) =
        getApplication()?.invokeLater {
            Notifications.Bus.notify(errorNotification(title, message))
        }

    fun errorNotification(title: String, message: String): Notification =
        Notification("Solhint Messages", title, message, ERROR)
            .addAction(FixIncorrectNodePathAction)
            .setImportant(true)

}

private object FixIncorrectNodePathAction : AnAction("Fix") {

    override fun actionPerformed(e: AnActionEvent?) {
        val settings = ShowSettingsUtil.getInstance()
        val solhintSettingsPage = settings.findApplicationConfigurable(SettingsPage::class.java)

        settings.editConfigurable(null, solhintSettingsPage, null)
    }

}
