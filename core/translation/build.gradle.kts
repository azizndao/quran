@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
  id("muslimapp.android.library")
  id("kotlinx-serialization")
  alias(libs.plugins.ksp)
}

android {
  namespace = "org.quran.translation"
  defaultConfig {
    ksp {
      arg("room.schemaLocation", "$projectDir/schemas")
    }
  }
}

dependencies {
  implementation(project(":core:common"))
  implementation(project(":core:network"))
  implementation(project(":core:datastore"))
  implementation(project(":core:models"))

  implementation(libs.room.ktx)
  ksp(libs.room.compiler)
}