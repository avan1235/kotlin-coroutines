package details

import kotlinx.coroutines.future.await
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletableFuture.completedFuture

suspend fun main() = stateMachine()

suspend fun stateMachine() {
    val a = a()
    val y = foo(a).await() // 1st suspension point
    b()
    val z = bar(a, y).await() // 2nd suspension point
    c(z)
}

fun a(): String = "A"
fun b(): Unit = Unit
fun c(z: String): Unit = println(z)

fun foo(a: String): CompletableFuture<String> = completedFuture(a)
fun bar(a: String, y: String): CompletableFuture<String> = completedFuture(a + y)