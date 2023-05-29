plugins {
  id("quran.android.feature")
  id("quran.android.library.compose")
}

android {
  namespace = "org.quran.features.home"

}

dependencies {
  implementation(project(":core:verses"))
  implementation(project(":core:bookmarks"))
}