package idrabenia.solhint.common

import com.intellij.notification.Notification
import com.intellij.notification.NotificationListener
import com.intellij.notification.NotificationType.ERROR
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager.getApplication
import com.intellij.openapi.options.ShowSettingsUtil
import idrabenia.solhint.settings.ui.SettingsPage
import javax.swing.event.HyperlinkEvent


object IdeMessages {

    fun notifyThatNodeNotInstalled() =
        error(
            "Node.js is not found",
            "Please provide correct path to Node.js. <a href=\"settings\">Fix</a>"
        )

    fun notifyThatSolhintNotInstalled() =
        error(
            "Solhint is not installed",
            "For correct run of Solidity Solhint Plugin you need to install Solhint. <a href=\"settings\">Fix</a>"
        )

    fun error(title: String, message: String) =
        getApplication()?.invokeLater {
            Notifications.Bus.notify(errorNotification(title, message))
        }

    fun errorNotification(title: String, message: String): Notification =
        Notification("Solhint Messages", title, message, ERROR, OpenSettingsListener)
            .setImportant(true)

}


private object OpenSettingsListener : NotificationListener.Adapter() {

    override fun hyperlinkActivated(notification: Notification, event: HyperlinkEvent) {
        ShowSettingsUtil
            .getInstance()
            .editConfigurable(null, SettingsPage(), null)
    }

}


private object FixIncorrectNodePathAction : AnAction("Fix") {

    override fun actionPerformed(e: AnActionEvent?) {
        ShowSettingsUtil
            .getInstance()
            .editConfigurable(null, SettingsPage(), null)
    }

}
