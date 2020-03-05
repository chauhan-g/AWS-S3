package objects

import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.ListObjectsV2Request

class ListObjects {
    fun listObjects(bucketName: String) {
        val s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_WEST_1).build()
        val prefix = "/folder/"
        val listObjectsV2Request = ListObjectsV2Request()
                .withBucketName(bucketName)
                .withPrefix(prefix)
                .withDelimiter("/")
//              .withMaxKeys(3)
        val result = s3Client.listObjectsV2(listObjectsV2Request)

        do {
            result.commonPrefixes.forEach { println(it) }
            println("------------")
            for (objectSummary in result.objectSummaries) {
                println(objectSummary.key)
            }
            // If there are more than maxKeys keys in the bucket, get a continuation token
            // and list the next objects.
            listObjectsV2Request.continuationToken = result.nextContinuationToken
            println("continuation token in request = " + listObjectsV2Request.continuationToken)
        } while (result.isTruncated)
    }

    fun listObjectsUsingContinuationToken(bucketName: String, continuationToken: String): List<String> {
        val s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_WEST_1).build()
        val prefix = "5dc56846-6abe-484b-bb96-84cdae42192a/folder/"
        val listObjectsV2Request = ListObjectsV2Request()
                .withBucketName(bucketName)
                .withPrefix(prefix)
                .withDelimiter("/")
//              .withMaxKeys(3)
                .withContinuationToken(continuationToken)

        println(listObjectsV2Request.continuationToken)

        val lzContent: MutableList<String> = mutableListOf()

        val result = s3Client.listObjectsV2(listObjectsV2Request)
        result.commonPrefixes.forEach { lzContent.add(it.replaceFirst(prefix, "")) }
        result.objectSummaries.forEach { if (prefix != it.key) lzContent.add(it.key.replaceFirst(prefix, "")) }
        println("lz content is :$lzContent")
        // If there are more than maxKeys keys in the bucket, get a continuation token
        // and list the next objects.
        listObjectsV2Request.continuationToken = result.nextContinuationToken
        println("continuation token in request = " + listObjectsV2Request.continuationToken)

        return lzContent
    }
}

fun main() {
    ListObjects().listObjects("gaurav-s3-playground")
    ListObjects().listObjectsUsingContinuationToken("gaurav-s3-playground", "1dh5gzKTLJn70+smGypsy0wPiVY3azW+nMcOW9yYOEGh2FFgEHuaCxfMwfkQu5K+EwQ+waUgDwpfOKrrU+hz5Jv+EG87r1Y6F5kAFOpFwCQ4jW4yvmFCpmIlxZSmUk8YXgMbNYkJBd9bosN+PIFxFcQ==")
}