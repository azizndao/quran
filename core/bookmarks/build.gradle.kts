@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
  id("quran.android.library")
  id("kotlinx-serialization")
  alias(libs.plugins.ksp)
}

android {
  namespace = "org.quran.bookmarks"

  defaultConfig {
    ksp {
      arg("room.schemaLocation", "$projectDir/schemas")
    }
  }
}

dependencies {
  implementation(project(":core:models"))

  implementation(libs.kotlinx.serialization)

  implementation(libs.room.ktx)
  ksp(libs.room.compiler)
}