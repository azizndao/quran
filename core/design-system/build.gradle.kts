plugins {
  id("quran.android.library")
  id("quran.android.library.compose")
}

android {
  namespace = "org.quran.ui"
}

dependencies {
  api(libs.androidx.compose.material3)
  api(libs.androidx.compose.material3.windowSizeClass)
  api(libs.androidx.compose.ui.util)
  api(libs.androidx.compose.ui.tooling.preview)
  api(libs.androidx.compose.animation.graphics)
  api(libs.lifecycle.runtime.ktx)
  api(libs.lifecycle.runtime.compose)

  implementation(libs.androidx.compose.ui.text.google.fonts)

  androidTestApi(libs.androidx.compose.ui.test)
  debugApi(libs.androidx.compose.ui.tooling)
  debugApi(libs.androidx.compose.ui.testManifest)
}