package idrabenia.solhint.common

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit


object Debouncer {
    private val scheduler = Executors.newSingleThreadScheduledExecutor()
    private val delayedMap = ConcurrentHashMap<Any, Future<*>>()

    init {
        Runtime.getRuntime().addShutdownHook(Thread { this.shutdown() })
    }

    fun debounce(key: Any, runnable: Runnable, delay: Long, unit: TimeUnit) {
        val future = scheduler.schedule({ tryExecute(key, runnable) }, delay, unit)
        val prev = delayedMap.put(key, future)
        prev?.cancel(true)
    }

    private fun tryExecute(key: Any, runnable: Runnable) {
        try {
            runnable.run()
        } finally {
            delayedMap.remove(key)
        }
    }

    fun shutdown() {
        scheduler.shutdownNow();
    }
}
