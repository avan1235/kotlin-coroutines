package details

import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO

fun main() = runBlocking {
    implicitScope()
    explicitScope()
}

suspend fun implicitScope(): Unit = coroutineScope {
    launch(context = Default) { say("hello") }
    launch(context = IO) { say("world") }
}

suspend fun explicitScope(): Unit = coroutineScope {
    val scope: CoroutineScope = this
    scope.launch(context = Default) { say("hello") }
    scope.launch(context = IO) { say("world") }
}

suspend fun say(msg: String, timeMillis: Long = 1000) {
    msg.forEach {
        delay(timeMillis)
        print(it)
    }
    println()
}