package bucket

import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3ClientBuilder

fun main() {
    val s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_WEST_1).build()
    val bucketList = s3Client.listBuckets()
    bucketList.iterator().forEach { println(it.name) }
}