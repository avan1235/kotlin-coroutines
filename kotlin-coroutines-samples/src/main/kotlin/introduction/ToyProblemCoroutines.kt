package introduction

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    processRequestCoroutines()
}

suspend fun processRequestCoroutines() {
    val request = receiveRequestCoroutines()
    val processed = handleRequestCoroutines(request)
    sendResponseCoroutines(processed)
}

suspend fun sendResponseCoroutines(processed: Response) = delayed {
    println(processed)
}

suspend fun receiveRequestCoroutines(): Request = delayed {
    Request("test-user")
}

suspend fun handleRequestCoroutines(request: Request): Response = delayed {
    Response("hello ${request.body}")
}

private suspend inline fun <T> delayed(timeMillis: Long = 1_000, producer: () -> T): T {
    delay(timeMillis)
    return producer()
}
