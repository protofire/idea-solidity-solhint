package idrabenia.solhint.client.process


class EmptyProcess() : AbstractSolhintProcess {
    override val process = null
    override val port = 55000

    override fun isAlive() = true

    override fun stop() {
        // noop
    }
}
