package details

import introduction.handleRequestPromise
import introduction.receiveRequestPromise
import introduction.sendResponse
import io.reactivex.rxjava3.core.SingleSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.rx3.await

fun main() = processRequest()

fun processRequest() = async {
    val request = await(receiveRequestPromise())
    val processed = await(handleRequestPromise(request))
    sendResponse(processed)
}

private fun async(action: CoroutineScope.() -> Unit): Unit =
    runBlocking { action() }

private fun <T : Any> CoroutineScope.await(value: SingleSource<T>): T =
    runBlocking(coroutineContext) { value.await() }