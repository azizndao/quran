plugins {
  id("quran.android.feature")
  id("quran.android.library.compose")
}

android {
  namespace = "org.quran.features.saved"
}

dependencies {
  implementation(project(":core:bookmarks"))
}