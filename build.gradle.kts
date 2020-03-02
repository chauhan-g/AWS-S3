import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

val awsSDKVersion = "1.11.628"

group = "AWSPlayground.S3"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "1.3.50"
    id("com.github.johnrengelman.shadow") version "5.1.0"
    id("com.github.ben-manes.versions") version "0.24.0"
}

repositories {
    mavenCentral()
    maven {
        url = URI("s3://dl-049-repos")
        credentials(AwsCredentials::class.java) {
            setAccessKey(properties["AWSKEY049"] as String)
            setSecretKey(properties["AWSSECRETKEY049"] as String)
        }
    }
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile("com.amazonaws:aws-lambda-java-core:1.2.0")
    compile("com.amazonaws:aws-lambda-java-events:2.2.7")
    compile("com.amazonaws:aws-java-sdk-s3:$awsSDKVersion")
    compile("com.amazonaws:aws-java-sdk-lambda:$awsSDKVersion")
    compile("com.amazonaws:aws-java-sdk-dynamodb:$awsSDKVersion")
    compile("com.amazonaws:aws-java-sdk-sns:$awsSDKVersion")
    compile("com.amazonaws:aws-java-sdk-transfer:$awsSDKVersion")
    compile("com.amazonaws:aws-java-sdk-iam:$awsSDKVersion")
    compile("com.amazonaws:aws-java-sdk-sts:$awsSDKVersion")
    compile("com.amazonaws:aws-java-sdk-secretsmanager:$awsSDKVersion")
    compile("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.10.0.pr2")
    compile("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.0.pr2")
    compile("org.apache.commons:commons-lang3:3.9")

    compile("com.skf.datalake.util:dl-util:0.1.1")

    testCompile("org.junit.jupiter:junit-jupiter-api:5.5.2")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:5.5.2")
    testCompile("io.mockk:mockk:1.9.3")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}