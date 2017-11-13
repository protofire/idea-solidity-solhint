package idrabenia.solhint.client.process


interface AbstractSolhintProcess {
    val process: Process?

    fun isAlive(): Boolean

    fun stop()

}
