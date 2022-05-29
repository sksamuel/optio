plugins {
   id("java")
   kotlin("multiplatform")
   id("java-library")
}

repositories {
   mavenCentral()
}

kotlin {

   targets {
      jvm {
         compilations.all {
            kotlinOptions {
               jvmTarget = "11"
            }
         }
      }
   }

   sourceSets {

      val commonMain by getting {
         dependencies {
            api(project(":optio-core"))
         }
      }

      val jvmMain by getting {
         dependencies {
            implementation(project(":optio-ktor"))
            implementation(Ktor.server.netty)
            implementation(Ktor.client.cio)
            api("io.ktor:ktor-serialization-jackson:_")
            api("io.ktor:ktor-server-content-negotiation:_")
            api("io.ktor:ktor-client-content-negotiation:_")
         }
      }

      all {
         languageSettings.optIn("kotlin.OverloadResolutionByLambdaReturnType")
      }
   }
}

tasks.named<Test>("jvmTest") {
   useJUnitPlatform()
   filter {
      isFailOnNoMatchingTests = false
   }
   testLogging {
      showExceptions = true
      showStandardStreams = true
      events = setOf(
         org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
         org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
      )
      exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
   }
}

apply(from = "../publish-mpp.gradle.kts")