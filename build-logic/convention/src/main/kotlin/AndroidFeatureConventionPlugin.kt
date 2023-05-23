import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidFeatureConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      pluginManager.apply {
        apply("muslimapp.android.library")
      }
      extensions.configure<LibraryExtension> {
        defaultConfig {
          testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
      }

      val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

      dependencies {
        add("implementation", project(":core:common"))
        add("implementation", project(":core:models"))
        add("implementation", project(":core:design-system"))
        add("implementation", project(":core:datastore"))
        add("implementation", project(":core:navigation"))

        add("testImplementation", libs.findLibrary("junit").get())
        add("androidTestImplementation", libs.findLibrary("androidx.test.ext.junit").get())
        add("androidTestImplementation", libs.findLibrary("espresso.core").get())

        add("implementation", libs.findLibrary("koin.compose").get())

        add("implementation", libs.findLibrary("glide.compose").get())

        add("implementation", libs.findLibrary("datastore-preferences").get())

        add("implementation", libs.findLibrary("kotlinx.collections.immutable").get())

        add("implementation", libs.findLibrary("lifecycle.runtime.compose").get())
        add("implementation", libs.findLibrary("lifecycle.runtime.ktx").get())

        add("implementation", libs.findLibrary("kotlinx.coroutines.android").get())
      }
    }
  }
}
