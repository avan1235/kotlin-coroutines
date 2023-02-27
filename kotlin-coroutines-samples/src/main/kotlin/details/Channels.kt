package details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import java.io.ByteArrayInputStream
import java.io.InputStream

private const val N_WORKERS: Int = 3

fun CoroutineScope.processUrls(
    receiveStringUrlChannel: ReceiveChannel<String>,
    sendResultByteArrayChannel: SendChannel<ByteArray>,
) {
    val urls = Channel<Url>(capacity = 1)
    val data = Channel<DownloadedData>(capacity = 1)
    repeat(N_WORKERS) { downloadWorker(urls, data) }
    downloader(receiveStringUrlChannel, sendResultByteArrayChannel, urls, data)
}

fun CoroutineScope.downloader(
    receiveStringUrlChannel: ReceiveChannel<String>,
    sendResultByteArrayChannel: SendChannel<ByteArray>,
    sendToWorkerChannel: SendChannel<Url>,
    receiveFromWorkerChannel: ReceiveChannel<DownloadedData>,
) = launch {
    val cached = mutableMapOf<Url, ByteArray>()
    while (isActive) {
        select {
            receiveFromWorkerChannel.onReceive { (url, data) ->
                cached[url] = data
                sendResultByteArrayChannel.send(data)
            }
            receiveStringUrlChannel.onReceive {
                val url = Url(it)
                val data = cached[url]
                if (data == null) sendToWorkerChannel.send(url)
                else sendResultByteArrayChannel.send(data)
            }
        }
    }
}

fun CoroutineScope.downloadWorker(
    receive: ReceiveChannel<Url>,
    sendData: SendChannel<DownloadedData>,
) = launch {
    val client = HttpClient(CIO)
    for (url in receive) {
        val data = client.get(url)
        val bytes = data.readBytes()
        val downloaded = DownloadedData(url, bytes)
        sendData.send(downloaded)
    }
}

data class DownloadedData(
    val url: Url,
    val data: ByteArray,
)

fun main() = application {
    val scope = rememberCoroutineScope()
    val urlChannel = Channel<String>(capacity = 1)
    val imageDataChannel = Channel<ByteArray>(capacity = 1)
    LaunchedEffect(Unit) { processUrls(urlChannel, imageDataChannel) }
    // application ui
    Window(
        onCloseRequest = ::exitApplication,
        title = "Image view",
        state = rememberWindowState(width = 640.dp, height = 480.dp)
    ) {
        var url by remember { mutableStateOf("") }
        val imageData by imageDataChannel.receiveAsFlow<ByteArray?>().collectAsState(null)
        MaterialTheme {
            Column(
                modifier = Modifier.padding(8.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    TextField(
                        value = url,
                        onValueChange = { url = it },
                        modifier = Modifier.fillMaxWidth(0.8f),
                        singleLine = true,
                    )
                    Button(
                        onClick = { scope.launch { urlChannel.send(url) } },
                    ) {
                        Text("Download")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                imageData?.let { ImageFromByteArray(it) }
            }
        }
    }
}

@Composable
private fun ImageFromByteArray(byteArray: ByteArray) {
    val inputStream: InputStream = ByteArrayInputStream(byteArray)
    val bitmap = inputStream.use { loadImageBitmap(it) }
    Image(
        bitmap = bitmap, contentDescription = null,
        modifier = Modifier
            .aspectRatio(bitmap.run { width.toFloat() / height.toFloat() })
            .fillMaxWidth(),
    )
}

/*
interface SendChannel<T> {
    suspend fun send(value: T)
    fun close()
}

interface ReceiveChannel<T> {
    suspend fun receive(): T
    suspend operator fun iterator(): ReceiveIterator<T>
}
 */