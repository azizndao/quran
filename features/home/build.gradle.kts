plugins {
  id("muslimapp.android.feature")
  id("muslimapp.android.library.compose")
}

android {
  namespace = "org.quran.features.home"

}

dependencies {
  implementation(project(mapOf("path" to ":core:verses")))
}