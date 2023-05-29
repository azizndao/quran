plugins {
  id("quran.android.library")
}

android {
  namespace = "org.quran.network"
}

dependencies {
  api(libs.kotlinx.serialization)

  api(libs.koin.workmanager)

  api(libs.androidx.work)

  api(libs.retrofit)
  api(libs.retrofit.kotlinx)
}
