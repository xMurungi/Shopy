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
        val firebaseWebClientId = gradleLocalProperties(rootDir).getProperty("FIREBASE_WEB_CLIENT_ID")

        when (extensionType) {
            ExtensionType.APPLICATION -> {
                extensions.configure<ApplicationExtension>() {
                    buildTypes {
                        debug {
                            configureDebugBuildType(baseUrl, firebaseWebClientId)
                        }
                        release {
                            configureReleaseBuildType(commonExtension, baseUrl, firebaseWebClientId)
                        }
                    }
                }
            }

            ExtensionType.LIBRARY -> {
                extensions.configure<LibraryExtension>() {
                    buildTypes {
                        debug {
                            configureDebugBuildType(baseUrl, firebaseWebClientId)
                        }
                        release {
                            configureReleaseBuildType(commonExtension, baseUrl, firebaseWebClientId)
                        }
                    }
                }
            }
        }
    }
}

private fun BuildType.configureDebugBuildType(
    baseUrl: String,
    firebaseWebClientId: String
) {
    buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
    buildConfigField("String", "FIREBASE_WEB_CLIENT_ID", "\"$firebaseWebClientId\"")
}

private fun BuildType.configureReleaseBuildType(
    commonExtension: CommonExtension<*, *, *, *, *>,
    baseUrl: String,
    firebaseWebClientId: String
) {
    buildConfigField("String", "FIREBASE_WEB_CLIENT_ID", "\"$baseUrl\"")

    isMinifyEnabled = true
    proguardFiles(
        commonExtension.getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
    )
}