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

        val paymentsBaseUrl = gradleLocalProperties(rootDir).getProperty("PAYMENTS_SERVER_BASE_URL")
        val productsApiBaseUrl = gradleLocalProperties(rootDir).getProperty("PRODUCTS_API_BASE_URL")
        val firebaseWebClientId = gradleLocalProperties(rootDir).getProperty("FIREBASE_WEB_CLIENT_ID")

        when (extensionType) {
            ExtensionType.APPLICATION -> {
                extensions.configure<ApplicationExtension>() {
                    buildTypes {
                        debug {
                            configureDebugBuildType(
                                paymentsBaseUrl = paymentsBaseUrl,
                                productsApiBaseUrl = productsApiBaseUrl,
                                firebaseWebClientId = firebaseWebClientId
                            )
                        }
                        release {
                            configureReleaseBuildType(
                                commonExtension = commonExtension,
                                paymentsBaseUrl = paymentsBaseUrl,
                                productsApiBaseUrl = productsApiBaseUrl,
                                firebaseWebClientId = firebaseWebClientId
                            )
                        }
                    }
                }
            }

            ExtensionType.LIBRARY -> {
                extensions.configure<LibraryExtension>() {
                    buildTypes {
                        debug {
                            configureDebugBuildType(
                                paymentsBaseUrl = paymentsBaseUrl,
                                productsApiBaseUrl = productsApiBaseUrl,
                                firebaseWebClientId = firebaseWebClientId
                            )
                        }
                        release {
                            configureReleaseBuildType(
                                commonExtension = commonExtension,
                                paymentsBaseUrl = paymentsBaseUrl,
                                productsApiBaseUrl = productsApiBaseUrl,
                                firebaseWebClientId = firebaseWebClientId
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun BuildType.configureDebugBuildType(
    paymentsBaseUrl: String,
    productsApiBaseUrl: String,
    firebaseWebClientId: String
) {
    buildConfigField("String", "PAYMENTS_SERVER_BASE_URL", "\"$paymentsBaseUrl\"")
    buildConfigField("String", "PRODUCTS_API_BASE_URL", "\"$productsApiBaseUrl\"")
    buildConfigField("String", "FIREBASE_WEB_CLIENT_ID", "\"$firebaseWebClientId\"")
}

private fun BuildType.configureReleaseBuildType(
    commonExtension: CommonExtension<*, *, *, *, *>,
    paymentsBaseUrl: String,
    productsApiBaseUrl: String,
    firebaseWebClientId: String
) {
    buildConfigField("String", "PAYMENTS_SERVER_BASE_URL", "\"$paymentsBaseUrl\"")
    buildConfigField("String", "PRODUCTS_API_BASE_URL", "\"$productsApiBaseUrl\"")
    buildConfigField("String", "FIREBASE_WEB_CLIENT_ID", "\"$firebaseWebClientId\"")

    isMinifyEnabled = false
    proguardFiles(
        commonExtension.getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
    )
}