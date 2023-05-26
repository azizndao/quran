plugins {
  id("muslimapp.android.feature")
  id("muslimapp.android.library.compose")
}

android {
  namespace = "org.quran.features.pager"
}

dependencies {
  implementation(project(":core:audio"))
  implementation(project(":core:translation"))
  implementation(project(":core:bookmarks"))
  implementation(project(":core:verses"))
}