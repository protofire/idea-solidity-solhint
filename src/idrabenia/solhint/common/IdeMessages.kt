package idrabenia.solhint.common

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType.WARNING
import com.intellij.notification.Notifications
import com.intellij.openapi.application.ApplicationManager


object IdeMessages {

    fun notifyThatNodeNotInstalled() =
        error(
            "Node.js is not installed",
            "For correct run of Solidity Solhint Plugin you need to install Node.js"
        )

    fun notifyThatSolhintNotInstalled() =
        error(
            "Solhint is not installed",
            "For correct run of Solidity Solhint Plugin you need to install Solhint. Just run 'npm install -g solhint'"
        )

    fun error(title: String, message: String) =
        if (ApplicationManager.getApplication() != null) {
            Notifications.Bus.notify(Notification("idrabenia.solidity-solhint", title, message, WARNING))
        } else {
            null
        }

}
