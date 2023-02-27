package introduction

fun main() = recursiveCall(deep = 1688)

fun recursiveCall(deep: Int): Unit =
    if (deep < 1) Unit else recursiveCall(deep - 1)
