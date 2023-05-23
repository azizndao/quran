plugins {
  id("muslimapp.android.feature")
  id("muslimapp.android.library.compose")
}

android {
  namespace = "org.quran.features.share"
}

dependencies {
  implementation(project(":core:common"))
  implementation(project(":core:verses"))
  implementation(project(":core:translation"))
}