plugins {
  id("quran.android.feature")
  id("quran.android.library.compose")
}

android {
  namespace = "org.quran.features.reciter"
}

dependencies {
  implementation(project(mapOf("path" to ":core:audio")))
}