package details

import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.coroutines.coroutineContext

suspend fun main() = retry(afterMillis = 10) { say("hello") }

suspend fun retry(afterMillis: Long, action: suspend () -> Unit) {
    do {
        try {
            return action()
        } catch (e: Exception) {
            println(e.stackTrace)
        }
        delay(afterMillis)
    } while (coroutineContext.isActive)
}
