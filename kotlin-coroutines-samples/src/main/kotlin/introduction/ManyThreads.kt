package introduction

import java.lang.Thread.sleep
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread

fun main() {
    val counter = AtomicInteger(0)
    List(100_000) {
        thread {
            sleep(1_000)
            counter.incrementAndGet()
        }
    }.forEach { it.join() }
    println("counter = ${counter.get()}")
}