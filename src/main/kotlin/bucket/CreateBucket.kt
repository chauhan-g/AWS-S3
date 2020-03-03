package bucket

import com.amazonaws.AmazonServiceException
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.CreateBucketRequest
import com.amazonaws.services.s3.model.GetBucketLocationRequest

class CreateBucket {
    fun createBucket(bucketName: String) {
        val clientRegion = Regions.EU_WEST_1
        try {
            val s3Client = AmazonS3ClientBuilder
                    .standard()
                    .withCredentials(ProfileCredentialsProvider())
                    .withRegion(clientRegion)
                    .build()
            if (!s3Client.doesBucketExistV2(bucketName)) {
                s3Client.createBucket(CreateBucketRequest(bucketName))
                val bucketLoation = s3Client.getBucketLocation(GetBucketLocationRequest(bucketName))
                println(bucketLoation)
            }
        } catch (e: AmazonServiceException) {
            println(e.errorMessage)
        }
    }
}

fun main(args: Array<String>) {
    CreateBucket().createBucket(bucketName = "gaurav-s3-playground")
}