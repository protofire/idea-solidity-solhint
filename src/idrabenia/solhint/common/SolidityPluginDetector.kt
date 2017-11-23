package idrabenia.solhint.common

import com.intellij.ide.plugins.PluginManager
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.project.ProjectManagerListener

/**
 * @author Ilya Drabenia
 */
class SolidityPluginDetector {

    init {
        ProjectManager.getInstance()
            .addProjectManagerListener(DetectorProjectListener({
                validateThatSolidityPluginInstalled()
            }))
    }

    private fun validateThatSolidityPluginInstalled() =
        if (!isSoliditySupportInstalled()) {
            IdeMessages.notifyThatSolidityPluginNotInstalled()!!
        } else {
            // noop
        }

    private fun isSoliditySupportInstalled() =
        PluginManager
            .getPlugins()
            .filter { it.pluginId == PluginId.getId("me.serce.solidity") && it.isEnabled }
            .any()

    open class DetectorProjectListener(val listener: () -> Unit) : ProjectManagerListener {
        override fun projectClosing(project: Project?) { /* noop */ }
        override fun projectClosed(project: Project?) { /* noop */ }
        override fun canCloseProject(project: Project?): Boolean = true
        override fun projectOpened(project: Project?) = listener.invoke()
    }

}
