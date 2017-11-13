package idrabenia.solhint.client

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.NotificationType.WARNING
import com.intellij.notification.Notifications
import com.intellij.openapi.application.ApplicationManager
import idrabenia.solhint.client.process.EmptyProcess
import idrabenia.solhint.client.process.ServerProcess


object Environment {

    init {
        validateDependencies()
    }

    fun validateDependencies() =
        if (!isNodeJsInstalled()) {
            notifyThatNodeNotInstalled()
        } else if (!isSolhintInstalled()) {
            notifyThatSolhintNotInstalled()
        } else {
            // noop
        }

    fun solhintServer(baseDir: String) =
        if (isSolhintInstalled()) {
            ServerProcess(baseDir)
        } else {
            EmptyProcess()
        }

    fun isNodeJsInstalled() =
        canRunProcess("node -v")

    fun isSolhintInstalled() =
        canRunProcess("solhint")

    fun notifyThatNodeNotInstalled() =
        notify(
            "idrabenia.solidity-solhint",
            "Node.js is not installed",
            "For correct run of Solidity Solhint Plugin you need to install Node.js",
            WARNING
        )

    fun notifyThatSolhintNotInstalled() =
        notify(
            "idrabenia.solidity-solhint",
            "Solhint is not installed",
            "For correct run of Solidity Solhint Plugin you need to install Solhint. Just run 'npm install -g solhint'",
            WARNING
        )

    fun notify(id: String, title: String, message: String, type: NotificationType) =
        if (ApplicationManager.getApplication() != null) {
            Notifications.Bus.notify(Notification(id, title, message, type))
        } else {
            null
        }

    fun canRunProcess(cmd: String) =
        try {
            Runtime.getRuntime().exec(cmd).waitFor() == 0
        } catch (e: Exception) {
            false
        }

}