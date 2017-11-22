package idrabenia.solhint.common

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType.ERROR
import com.intellij.notification.Notifications
import com.intellij.openapi.application.ApplicationManager.getApplication
import idrabenia.solhint.settings.ui.OpenSettingsListener


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
