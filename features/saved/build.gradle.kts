plugins {
  id("muslimapp.android.feature")
  id("muslimapp.android.library.compose")
}

android {
  namespace = "org.quran.features.saved"
}

dependencies {
  implementation(project(":core:bookmarks"))
}