@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
  id("quran.android.library")
  alias(libs.plugins.protobuf)
  id("kotlinx-serialization")
}

android {
  namespace = "org.quran.datastore"

  defaultConfig {
    consumerProguardFiles("consumer-rules.pro")
  }
}

dependencies {
  implementation(libs.kotlinx.serialization)

  implementation(libs.datastore.core)
  api(libs.datastore.preferences)

  api(libs.protobuf.kotlin.lite)
}

protobuf {
  protoc {
    artifact = libs.protoc.get().toString()
  }

  generateProtoTasks {
    all().forEach { task ->
      task.builtins {
        register("java") {
          option("lite")
        }
        register("kotlin") {
          option("lite")
        }
      }
    }
  }
}
