plugins {
  id("muslimapp.android.feature")
  id("muslimapp.android.library.compose")
}

android {
  namespace = "org.quran.features.verse_menu"
}

dependencies {
  implementation(project(":core:audio"))
  implementation(project(":core:bookmarks"))
}