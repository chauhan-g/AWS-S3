package bucket

import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.ListVersionsRequest

class DeleteBucket {

    fun deleteBucket(bucketName: String) {
        val clientRegion = Regions.EU_WEST_1
        val s3Client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(ProfileCredentialsProvider())
                .withRegion(clientRegion)
                .build()
        // for unversioned
        var objectListing = s3Client.listObjects(bucketName)
        while (true) {
            val objectIterator = objectListing.objectSummaries.iterator()
            while (objectIterator.hasNext()) {
                s3Client.deleteObject(bucketName, objectIterator.next().key)
            }

            // listObjects might not return all object.So retrieve next page and keep it doing
            if (objectListing.isTruncated) {
                objectListing = s3Client.listNextBatchOfObjects(objectListing)
            } else {
                break
            }
        }

        // delete all versions
        var versionList = s3Client.listVersions(ListVersionsRequest().withBucketName(bucketName))
        while (true) {
            val versionIterator = versionList.versionSummaries.iterator()
            while (versionIterator.hasNext()) {
                val s3VersionsSummary = versionIterator.next()
                s3Client.deleteVersion(bucketName, s3VersionsSummary.key, s3VersionsSummary.versionId)
            }

            if (versionList.isTruncated) {
                versionList = s3Client.listNextBatchOfVersions(versionList)
            } else {
                break
            }
        }

        // After all objects and object versions are deleted, delete the bucket.
        s3Client.deleteBucket(bucketName)

        // Implement try catch
    }
}

private fun main(args: Array<String>) {
    DeleteBucket().deleteBucket(bucketName = "gaurav-s3-playground")
}