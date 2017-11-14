package idrabenia.solhint.client.process


interface AbstractSolhintProcess {
    val process: Process?
    val port: Int

    fun isAlive(): Boolean

    fun stop()
}
