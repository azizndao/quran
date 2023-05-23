plugins {
  id("muslimapp.android.library")
  id("kotlinx-serialization")
  alias(libs.plugins.ksp)
  id("kotlin-parcelize")
}

android {
  namespace = "org.quran.core.audio"

  defaultConfig {
    ksp {
      arg("room.schemaLocation", "$projectDir/schemas")
    }
  }
}

dependencies {
  implementation(project(":core:common"))
  implementation(project(":core:datastore"))
  implementation(project(":core:network"))
  implementation(project(":core:models"))

  implementation(libs.androidx.ktx)

  implementation(libs.room.ktx)
  ksp(libs.room.compiler)

  api(libs.media3.exoplayer)
  implementation(libs.media3.exoplayer.workmanager)
  implementation(libs.media3.session)
  implementation(libs.media3.ui)
  implementation(libs.media3.datasource)
  implementation(libs.media3.datasource.okhttp)
  implementation(libs.media3.database)
}