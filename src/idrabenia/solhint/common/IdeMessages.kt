package idrabenia.solhint.common

import com.intellij.notification.Notification
import com.intellij.notification.NotificationListener
import com.intellij.notification.NotificationListener.URL_OPENING_LISTENER
import com.intellij.notification.NotificationType.ERROR
import com.intellij.notification.Notifications
import com.intellij.openapi.application.ApplicationManager.getApplication
import idrabenia.solhint.settings.ui.OpenSettingsListener


object IdeMessages {

    fun notifyThatNodeNotInstalled() =
        error(
            "Node.js is not found",
            "Please provide correct path to Node.js. <a href=\"settings\">Fix</a>",
            OpenSettingsListener
        )

    fun notifyThatSolhintNotInstalled() =
        error(
            "Solhint is not installed",
            "For correct run of Solidity Solhint Plugin you need to install Solhint. <a href=\"settings\">Fix</a>",
            OpenSettingsListener
        )

    fun notifyThatSolidityPluginNotInstalled() =
        error(
            "Solidity Plugin is not installed",
            "Please install " +
            "<a href=\"https://plugins.jetbrains.com/plugin/9475-intellij-solidity\">Solidity Plugin</a>. \n" +
            "It's required to correct evaluation of Solidity Solhint Extension",
            URL_OPENING_LISTENER
        )

    fun error(title: String, message: String, listener: NotificationListener) =
        getApplication()?.invokeLater {
            Notifications.Bus.notify(errorNotification(title, message, listener))
        }

    fun errorNotification(title: String, message: String, listener: NotificationListener): Notification =
        Notification("Solhint Messages", title, message, ERROR, listener)
            .setImportant(true)

}
