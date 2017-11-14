package idrabenia.solhint.client.process


class EmptyProcess() : AbstractSolhintProcess {
    override val process: Process? = null

    override fun isAlive() = true

    override fun stop() {
        // noop
    }
}
