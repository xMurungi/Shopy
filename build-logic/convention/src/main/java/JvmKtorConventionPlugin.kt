import com.ag_apps.build_logic.convention.configureKotlinJvm
import com.ag_apps.build_logic.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * @author Ahmed Guedmioui
 */
class JvmKtorConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

            dependencies {
                "implementation"(libs.findBundle("ktor").get())
            }
        }
    }

}