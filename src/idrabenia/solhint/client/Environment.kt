package idrabenia.solhint.client

import com.intellij.openapi.diagnostic.Logger
import idrabenia.solhint.common.IdeMessages.notifyThatNodeNotInstalled
import idrabenia.solhint.common.IdeMessages.notifyThatSolhintNotInstalled
import idrabenia.solhint.client.process.EmptyProcess
import idrabenia.solhint.client.process.ServerProcess
import idrabenia.solhint.common.IoStreams
import idrabenia.solhint.common.IoStreams.drain
import idrabenia.solhint.settings.data.SettingsManager.nodePath
import idrabenia.solhint.settings.data.SettingsManager.solhintPath
import java.io.File
import java.lang.System.getProperty
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.SECONDS


object Environment {
    val LOG = Logger.getInstance(Environment::class.java)

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
        isNodeJsInstalled(nodePath())

    fun isNodeJsInstalled(path: String) =
        canRunProcess("$path -v")

    fun isSolhintInstalled() =
        File(solhintPath()).exists()

    fun isSolhintInstalledInNode(nodePath: String) =
        File(nodePath)
            .resolveSibling("solhint")
            .exists()

    fun installSolhint(nodePath: String) =
        try {
            val process = ProcessBuilder()
                .directory(File(nodePath).parentFile)
                .command(nodePath, npmPath(File(nodePath)), "install", "-g", "solhint")
                .start()

            drain(process.inputStream)
            drain(process.errorStream)

            process.waitFor(5, TimeUnit.MINUTES)
        } catch (e: Exception) {
            LOG.warn("Could not install Solhint", e)
        }

    fun canRunProcess(cmd: String) =
        try {
            Runtime.getRuntime().exec(cmd).waitFor(2, SECONDS)
        } catch (e: Exception) {
            false
        }

    fun npmPath(nodeFile: File) =
        if (!getProperty("os.name").contains("windows", true)) {
            nodeFile
                .resolveSibling("npm")
                .absolutePath
        } else {
            nodeFile
                .parentFile
                .resolve("node_modules/npm/bin/npm-cli.js")
                .absolutePath
        }

}
