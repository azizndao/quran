plugins {
  id("muslimapp.android.feature")
  id("muslimapp.android.library.compose")
}

android {
  namespace = "org.muslimsapp.quran.translations"
}

dependencies {
  implementation(project(":core:translation"))
}