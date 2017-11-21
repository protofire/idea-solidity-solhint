package idrabenia.solhint.settings.data

import idrabenia.solhint.client.PathExecutableDetector.detectNodePath


class Settings(var nodePath: String) {

    constructor(): this(detectNodePath())

}