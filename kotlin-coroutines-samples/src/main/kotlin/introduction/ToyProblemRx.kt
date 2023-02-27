package introduction

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.subscribeBy

fun main() = processRequestPromise()

fun processRequestPromise() {
    receiveRequestPromise()
        .flatMap { handleRequestPromise(it) }
        .subscribeBy(
            onSuccess = { sendResponse(it) },
            onError = { println("error: $it") },
        )
}

fun receiveRequestPromise(): Single<Request> =
    Single.create { it.onSuccess(Request("test-user")) }

fun handleRequestPromise(request: Request): Single<Response> =
    Single.create { it.onSuccess(Response("hello ${request.body}")) }
