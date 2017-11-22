package idrabenia.solhint.settings.data

import idrabenia.solhint.client.NodePathDetector.detectNodePath


class Settings(var nodePath: String) {

    constructor(): this(detectNodePath())

}