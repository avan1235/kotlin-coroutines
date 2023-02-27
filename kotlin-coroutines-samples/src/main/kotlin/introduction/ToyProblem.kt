package introduction

fun main() = processRequest()

fun processRequest() {
    val request = receiveRequest()
    val processed = handleRequest(request)
    sendResponse(processed)
}

fun sendResponse(processed: Response) =
    println(processed)

fun receiveRequest(): Request =
    Request("test-user")

fun handleRequest(request: Request): Response =
    Response("hello ${request.body}")

data class Request(val body: String)
data class Response(val body: String)
