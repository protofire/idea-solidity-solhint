package idrabenia.solhint.client

import idrabenia.solhint.common.IdeMessages.notifyThatNodeNotInstalled
import idrabenia.solhint.common.IdeMessages.notifyThatSolhintNotInstalled
import idrabenia.solhint.client.process.EmptyProcess
import idrabenia.solhint.client.process.ServerProcess
import idrabenia.solhint.settings.data.SettingsManager.nodePath
import idrabenia.solhint.settings.data.SettingsManager.solhintPath
import java.io.File


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
            ServerProcess(nodePath(), baseDir)
        } else {
            EmptyProcess()
        }

    fun isNodeJsInstalled() =
        canRunProcess("${nodePath()} -v")

    fun isSolhintInstalled() =
        File(solhintPath()).exists()

    fun canRunProcess(cmd: String) =
        try {
            Runtime.getRuntime().exec(cmd).waitFor() == 0
        } catch (e: Exception) {
            false
        }

}
