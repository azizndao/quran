plugins {
  id("muslimapp.android.feature")
  id("muslimapp.android.library.compose")
}

android {
  namespace = "org.quran.features.settings"
}

dependencies {
  implementation(project(":core:datastore"))
}