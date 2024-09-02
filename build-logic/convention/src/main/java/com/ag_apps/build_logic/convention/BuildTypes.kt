package com.ag_apps.build_logic.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * @author Ahmed Guedmioui
 */

internal fun Project.configureBuildTypes(
    commonExtension: CommonExtension<*, *, *, *, *>,
    extensionType: ExtensionType
) {
    commonExtension.run {

        buildFeatures {
            buildConfig = true
        }

        val baseUrl = gradleLocalProperties(rootDir).getProperty("BASE_URL")
        when (extensionType) {
            ExtensionType.APPLICATION -> {
                extensions.configure<ApplicationExtension>() {
                    buildTypes {
                        debug {
                            configureDebugBuildType(baseUrl)
                        }
                        release {
                            configureReleaseBuildType(commonExtension, baseUrl)
                        }
                    }
                }
            }

            ExtensionType.LIBRARY -> {
                extensions.configure<LibraryExtension>() {
                    buildTypes {
                        debug {
                            configureDebugBuildType(baseUrl)
                        }
                        release {
                            configureReleaseBuildType(commonExtension, baseUrl)
                        }
                    }
                }
            }
        }
    }
}

private fun BuildType.configureDebugBuildType(baseUrl: String) {
    buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
}

private fun BuildType.configureReleaseBuildType(
    commonExtension: CommonExtension<*, *, *, *, *>,
    baseUrl: String
) {
    buildConfigField("String", "BASE_URL", "\"$baseUrl\"")

    isMinifyEnabled = true
    proguardFiles(
        commonExtension.getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
    )
}