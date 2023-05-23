plugins {
  id("muslimapp.android.library")
}

android {
  namespace = "org.quran.network"
}

dependencies {
  implementation(libs.timber)

  api(libs.kotlinx.serialization)

  api(libs.koin.android)
  api(libs.koin.workmanager)

  api(libs.androidx.work)

  api(libs.retrofit)
  api(libs.retrofit.kotlinx)
}
