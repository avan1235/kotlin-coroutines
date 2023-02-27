package details

import kotlinx.coroutines.*

suspend fun main() = runBlocking {
    justSuspensionPoints()
    launchFirstInScope()
    launchBothInScope()
    launchWithException()
    launchInSupervisorWithException()
}

suspend fun justSuspensionPoints(): Unit = coroutineScope {
    sayA()
    sayB()
}

suspend fun launchFirstInScope(): Unit = coroutineScope {
    launch { sayA() }
    sayB()
}

suspend fun launchBothInScope(): Unit = coroutineScope {
    launch { sayA() }
    launch { sayB() }
}

suspend fun launchWithException(): Unit = coroutineScope {
    launch { error("illegal to sayA") }
    launch { sayB() }
}

suspend fun launchInSupervisorWithException(): Unit = supervisorScope {
    launch { error("illegal to sayA") }
    launch { sayB() }
}

private suspend fun sayA() = say("aaaaa")

private suspend fun sayB() = say("bbbbb")
