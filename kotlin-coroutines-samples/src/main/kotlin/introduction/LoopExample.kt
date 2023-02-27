package introduction

fun imperative() {
    var number = 0
    for (i in 1..1_000)
        number = inc(number)
    println(number)
}

fun imperativeCallback() {
    class LateInitCallbackWrapper<T : Any> {
        lateinit var callback: (T) -> Unit
    }

    val calls = Array(1_000) { LateInitCallbackWrapper<Int>() }
    for (i in 0..998)
        calls[i].callback = { incWithCallback(it, calls[i + 1].callback::invoke) }
    calls[999].callback = { incWithCallback(it, ::println) }
    calls[0].callback(0)
}

fun inc(x: Int): Int = x + 1

fun incWithCallback(x: Int, consumer: (Int) -> Unit) = consumer(x + 1)
