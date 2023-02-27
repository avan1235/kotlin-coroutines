package details

import java.util.concurrent.CompletableFuture
import kotlin.coroutines.suspendCoroutine
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


suspend fun <T> CompletableFuture<T>.await(): T =
    suspendCoroutine { cont: Continuation<T> ->
        whenComplete { result, exception ->
            if (exception == null) cont.resume(result)
            else cont.resumeWithException(exception)
        }
    }

/*
fun <T> CompletableFuture<T>.await(continuation: Continuation<T>): Any? =

suspend fun <T> CompletableFuture<T>.await(): T =
    suspendCoroutine { cont: Continuation<T> ->
        (this as CompletableFuture<T>).whenComplete(
            object : BiConsumer<T, Throwable?> {
                override fun accept(result: T, exception: Throwable?) {
                    if (exception == null) cont.resume(result)
                    else cont.resumeWithException(exception)
                }
            }
        )
    }
 */