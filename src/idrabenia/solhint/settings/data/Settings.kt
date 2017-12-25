package idrabenia.solhint.settings.data

import idrabenia.solhint.env.path.NodePathDetector.detectNodePath
import idrabenia.solhint.env.path.SolhintPathDetector.detectSolhintPath


class Settings(var nodePath: String, var solhintPath: String) {

    constructor(): this(detectNodePath(), detectSolhintPath(detectNodePath()))

}
