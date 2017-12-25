package idrabenia.solhint.settings.data

import idrabenia.solhint.client.path.NodePathDetector.detectNodePath
import idrabenia.solhint.client.path.SolhintPathDetector.detectSolhintPath


class Settings(var nodePath: String, var solhintPath: String) {

    constructor(): this(detectNodePath(), detectSolhintPath(detectNodePath()))

}
