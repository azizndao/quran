plugins {
  id("muslimapp.android.library")
}

android {
  namespace = "org.muslimapp.core.navigation"
}

dependencies {
  api(libs.navigation.compose)
}