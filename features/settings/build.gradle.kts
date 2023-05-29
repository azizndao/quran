plugins {
  id("quran.android.feature")
  id("quran.android.library.compose")
}

android {
  namespace = "org.quran.features.settings"
}

dependencies {
  implementation(project(":core:datastore"))
}