plugins {
  id("quran.android.feature")
  id("quran.android.library.compose")
}

android {
  namespace = "org.quran.search"
}

dependencies {
  implementation(project(":core:common"))
  implementation(project(":core:verses"))
  implementation(project(":core:translation"))
}