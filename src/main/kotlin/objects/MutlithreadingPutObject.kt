package objects

import com.amazonaws.AmazonServiceException
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import java.io.File
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutionException

fun main() {
    val future1 = CompletableFuture.runAsync { putMultipleFileAsObject("<bucketName>", "1", 1, 30) }

    val future2 = CompletableFuture.runAsync { putMultipleFileAsObject("<bucketName>", "2", 31, 60) }

    val future3 = CompletableFuture.runAsync { putMultipleFileAsObject("<bucketName>", "3", 61, 90) }

    val future4 = CompletableFuture.runAsync { putMultipleFileAsObject("<bucketName>", "4", 91, 120) }

    val future = CompletableFuture.allOf(future1, future2, future3, future4)
    try {
        future.get() // this line waits for all to be completed
    } catch (e: InterruptedException) { // Handle
    } catch (e: ExecutionException) {
    }
}

fun putMultipleFileAsObject(bucketName: String, appKey: String, start: Int, end: Int): String {
    val s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_WEST_1).build()

    val output = "done"

    (start..end).forEach {
        val fileName = "file$it.txt"
        val filePath = "/Users/gauravchauhan/Documents/work/s3TempFiles/$fileName"
        val keyName = "01ada6e5-cf3f-4eab-b3ef-9dd400000$appKey/$fileName"

        try {
            println("uploading file $fileName")
            s3Client.putObject(bucketName, keyName, File(filePath))
        } catch (e: AmazonServiceException) {
            println(e.errorMessage)
            return e.errorMessage
        }
    }
    return output
}
