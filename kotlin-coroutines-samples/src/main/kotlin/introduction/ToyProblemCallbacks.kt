package introduction

fun main() = processRequestCallbacks()

fun processRequestCallbacks() {
    receiveRequest { request ->
        handleRequest(request) { processed ->
            sendResponse(processed)
        }
    }
}

fun receiveRequest(consumer: (Request) -> Unit) =
    consumer(Request("test-user"))

fun handleRequest(request: Request, consumer: (Response) -> Unit) =
    consumer(Response("hello ${request.body}"))

