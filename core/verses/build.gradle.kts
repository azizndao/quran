@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
  id("quran.android.library")
  id("kotlinx-serialization")
  alias(libs.plugins.ksp)
}

android {
  namespace = "org.alquran.verses"
  ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
  }
}

dependencies {
  implementation(project(":core:common"))
  implementation(project(":core:models"))
  implementation(project(":core:network"))
  implementation(project(":core:datastore"))

  implementation(libs.room.ktx)
  ksp(libs.room.compiler)
}