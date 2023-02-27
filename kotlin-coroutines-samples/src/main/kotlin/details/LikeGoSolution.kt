package details

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun main() = runBlocking {
    go()
    kt()
}

suspend fun go() = coroutineScope {
    go { printDelayed("go: fizz") }
    printDelayed("go: buzz")
}

suspend fun kt() = coroutineScope {
    launch { printDelayed("kt: fizz") }
    printDelayed("kt: buzz")
}

suspend fun printDelayed(msg: Any, times: Int = 5) {
    for (i in 1..times) {
        delay(1_000)
        println(msg)
    }
}

fun CoroutineScope.go(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit,
): Job = launch(context, start, block)

