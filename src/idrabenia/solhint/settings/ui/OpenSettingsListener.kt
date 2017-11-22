package idrabenia.solhint.settings.ui

import com.intellij.notification.Notification
import com.intellij.notification.NotificationListener
import com.intellij.openapi.options.ShowSettingsUtil
import javax.swing.event.HyperlinkEvent


object OpenSettingsListener : NotificationListener.Adapter() {

    override fun hyperlinkActivated(notification: Notification, event: HyperlinkEvent) {
        ShowSettingsUtil
            .getInstance()
            .editConfigurable(null, SettingsPage(), null)
    }

}
