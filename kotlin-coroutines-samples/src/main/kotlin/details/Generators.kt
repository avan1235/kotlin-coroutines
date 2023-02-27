package details

fun main() = fibonacci().take(42).forEach(::println)

fun fibonacci() = sequence {
    yield(1)
    var curr = 1
    var next = 1
    while (true) {
        yield(next)
        val tmp = curr + next
        curr = next
        next = tmp
    }
}