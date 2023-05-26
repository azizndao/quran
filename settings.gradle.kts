pluginManagement {
  includeBuild("build-logic")

  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }
}

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
  }
}

rootProject.name = "quran"
include(":app")
include(":core:datastore")
include(":core:audio")
include(":core:translation")
include(":core:network")
include(":core:navigation")
include(":core:common")
include(":core:verses")
include(":core:bookmarks")
include(":core:models")
include(":core:design-system")

include(":features:search")
include(":features:share")
include(":features:translations")
include(":features:pager")
include(":features:home")
include(":features:saved")
