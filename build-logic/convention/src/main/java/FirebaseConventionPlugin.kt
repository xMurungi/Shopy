import com.ag_apps.build_logic.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * @author Ahmed Guedmioui
 */
class FirebaseConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.apply("com.google.gms.google-services")

            dependencies {
                "implementation"(platform(libs.findLibrary("firebase.bom").get()))
                "implementation"(libs.findLibrary("firebase.auth").get())
                "implementation"(libs.findLibrary("firebase.firestore").get())
                "implementation"(libs.findLibrary("androidx.credentials.play.services.auth").get())
                "implementation"(libs.findLibrary("androidx.credentials").get())
                "implementation"(libs.findLibrary("googleid").get())
            }
        }
    }

}