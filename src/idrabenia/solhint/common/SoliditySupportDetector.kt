package idrabenia.solhint.common

import com.intellij.ide.plugins.PluginManager
import com.intellij.openapi.extensions.PluginId

/**
 * @author Ilya Drabenia
 */
object SoliditySupportDetector {

    init {
        validateThatSolidityPluginInstalled()
    }

    private fun validateThatSolidityPluginInstalled() =
        if (!isSoliditySupportInstalled()) {
            IdeMessages.notifyThatSolidityPluginNotInstalled()
        } else {
            // noop
        }

    private fun isSoliditySupportInstalled() =
        PluginManager
            .getPlugins()
            .filter { it.pluginId == PluginId.getId("me.serce.solidity") && it.isEnabled }
            .any()

}
