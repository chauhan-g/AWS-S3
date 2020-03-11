package objects

import com.amazonaws.AmazonServiceException
import com.amazonaws.SdkClientException
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import java.io.File
import java.nio.file.Paths


class PutObject {
    fun putFileAsObject(bucketName: String, filePath: String) {
        val s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_WEST_1).build()
        val filekeyName = Paths.get(filePath).fileName.toString()
        val keyName = "common/$filekeyName"
        try {
            s3Client.putObject(bucketName, keyName, File(filePath))
        } catch (e: AmazonServiceException) {
            println(e.errorMessage)
        }
    }

    fun putMultipleFileAsObject(bucketName: String) {

        val s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_WEST_1).build()

        (1..100).forEach {
            val fileName = "file$it.txt"
            val filePath = "/Users/gauravchauhan/Documents/work/s3TempFiles/$fileName"
            val keyName = "01ada6e5-cf3f-4eab-b3ef-9dd411111111/$fileName"

            try {
                println("uploading file $fileName")
                s3Client.putObject(bucketName, keyName, File(filePath))
            } catch (e: AmazonServiceException) {
                println(e.errorMessage)
            }
        }
    }

    fun putStringAsObject(bucketName: String) {
        val s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_WEST_1).build()
        val stringObjkeyName = "string-object-key"
        try {
            s3Client.putObject(bucketName, stringObjkeyName, "uploading string as an object to s3")
        } catch (e: AmazonServiceException) {
            println(e.errorMessage)
        }
    }

    fun putEmtptyObject(bucketName: String) {
        val s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_WEST_1).build()
        val stringObjkeyName = "LZ1"
        try {
            s3Client.putObject(bucketName, stringObjkeyName, "")
        } catch (e: AmazonServiceException) {
            println(e.errorMessage)
        }
    }

    fun putNullAsObject(bucketName: String) {
        val s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_WEST_1).build()
        val stringObjkeyName = "TestNullWithRequest/"
        try {
            // Commenting out becuase it will not compile
            // s3Client.putObject(bucketName, stringObjkeyName, null)
        } catch (e: AmazonServiceException) {
            println(e.errorMessage)
        }
    }

    fun putObjectUsingBasicAWSCredentials(bucketName: String) {
        val fileName = "file17.txt"
        val fileObjKeyName = "a5761e89-e3d2-4ecf-88b9-915e8eb1fa20/$fileName"
        val filePath = "FILE-PATH/$fileName"
        val fileToUpload = File(filePath)

        val awsCredentials = BasicAWSCredentials("ACCESS-KEY", "SECRET-KEY")

        try {
            val s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(AWSStaticCredentialsProvider(awsCredentials))
                    .build()

            // Upload a file as a new object with ContentType and title specified.
            val request = PutObjectRequest(bucketName, fileObjKeyName, fileToUpload)
            val metadata = ObjectMetadata()
            metadata.contentType = "plain/text"
            metadata.addUserMetadata("x-amz-meta-title", "someTitle")
            request.metadata = metadata
            s3Client.putObject(request)
        } catch (e: AmazonServiceException) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace()
        } catch (e: SdkClientException) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace()
        }
    }
}

fun main(args: Array<String>) {
    PutObject().putFileAsObject(args[0], args[1])
    PutObject().putStringAsObject("gaurav-s3-playground")
    PutObject().putEmtptyObject("gaurav-s3-playground")
    PutObject().putNullAsObject("gaurav-s3-playground")
    PutObject().putMultipleFileAsObject("gaurav-s3-playground")
    PutObject().putObjectUsingBasicAWSCredentials("gaurav-s3-playground")
}