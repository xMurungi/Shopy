import com.ag_apps.build_logic.convention.addUiLayerDependencies
import com.ag_apps.build_logic.convention.configureAndroidCompose
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

/**
 * @author Ahmed Guedmioui
 */
class AndroidFeatureUiConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("shopy.android.library.compose")
            }

           dependencies {
               addUiLayerDependencies(target)
           }
        }
    }

}