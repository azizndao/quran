plugins {
  id("quran.android.feature")
  id("quran.android.library.compose")
}

android {
  namespace = "org.muslimsapp.quran.translations"
}

dependencies {
  implementation(project(":core:translation"))
}