package introduction

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicInteger

fun main() = runBlocking {
    val counter = AtomicInteger(0)
    List(100_000) {
        launch {
            delay(1_000)
            counter.incrementAndGet()
        }
    }.forEach { it.join() }
    println("counter = ${counter.get()}")
}