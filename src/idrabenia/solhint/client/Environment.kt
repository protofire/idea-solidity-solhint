package idrabenia.solhint.client

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType.WARNING
import com.intellij.notification.Notifications
import com.intellij.openapi.application.ApplicationManager
import idrabenia.solhint.client.process.EmptyProcess
import idrabenia.solhint.client.process.ServerProcess


object Environment {
    val isNodeAvailable = isNodeJsInstalled()
    val isSolhintAvailable = isSolhintInstalled()

    init {
        validateDependencies()
    }

    fun validateDependencies() =
        if (!isNodeAvailable) {
            notifyThatNodeNotInstalled()
        } else if (!isSolhintAvailable) {
            notifyThatSolhintNotInstalled()
        } else {
            // noop
        }

    fun solhintServer(baseDir: String) =
        if (isSolhintAvailable) {
            ServerProcess(baseDir)
        } else {
            EmptyProcess()
        }

    fun isNodeJsInstalled() =
        canRunProcess("node -v")

    fun isSolhintInstalled() =
        canRunProcess("solhint")

    fun notifyThatNodeNotInstalled() =
        warn(
            "Node.js is not installed",
            "For correct run of Solidity Solhint Plugin you need to install Node.js"
        )

    fun notifyThatSolhintNotInstalled() =
        warn(
            "Solhint is not installed",
            "For correct run of Solidity Solhint Plugin you need to install Solhint. Just run 'npm install -g solhint'"
        )

    fun warn(title: String, message: String) =
        if (ApplicationManager.getApplication() != null) {
            Notifications.Bus.notify(Notification("idrabenia.solidity-solhint", title, message, WARNING))
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