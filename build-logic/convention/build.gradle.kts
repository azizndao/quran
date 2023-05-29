plugins {
  `kotlin-dsl`
}

group = "org.quran.buildlogic"

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11

  toolchain {
    languageVersion.set(JavaLanguageVersion.of(11))
  }
}

dependencies {
  compileOnly(libs.android.gradlePlugin)
  compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
  plugins {
    register("androidApplicationCompose") {
      id = "quran.android.application.compose"
      implementationClass = "AndroidApplicationComposeConventionPlugin"
    }
    register("androidApplication") {
      id = "quran.android.application"
      implementationClass = "AndroidApplicationConventionPlugin"
    }
    register("androidLibraryCompose") {
      id = "quran.android.library.compose"
      implementationClass = "AndroidLibraryComposeConventionPlugin"
    }
    register("androidLibrary") {
      id = "quran.android.library"
      implementationClass = "AndroidLibraryConventionPlugin"
    }
    register("androidFeature") {
      id = "quran.android.feature"
      implementationClass = "AndroidFeatureConventionPlugin"
    }
    register("androidTest") {
      id = "quran.android.test"
      implementationClass = "AndroidTestConventionPlugin"
    }
  }
}
