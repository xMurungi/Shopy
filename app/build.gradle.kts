plugins {
    alias(libs.plugins.shopy.android.application.compose)
    alias(libs.plugins.shopy.jvm.ktor)
    alias(libs.plugins.gms.google.services)
    alias(libs.plugins.shopy.firebase)
}

android {
    namespace = "com.ag_apps.shopy"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Coil
    implementation(libs.coil.compose)

    // Compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)

    // Koin
    implementation(libs.bundles.koin)

    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Crypto
    implementation(libs.androidx.security.crypto.ktx)

    api(libs.core)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Splash screen
    implementation(libs.androidx.core.splashscreen)

    // Timber
    implementation(libs.timber)

    implementation(projects.core.data)
    implementation(projects.core.network)
    implementation(projects.core.productData)
    implementation(projects.core.domain)
    implementation(projects.core.presentation.designsystem)
    implementation(projects.core.presentation.ui)

    implementation(projects.auth.domain)
    implementation(projects.auth.data)
    implementation(projects.auth.presentation)

    implementation(projects.product.domain)
    implementation(projects.product.data)
    implementation(projects.product.presentation)

    implementation(projects.category.domain)
    implementation(projects.category.data)
    implementation(projects.category.presentation)

    implementation(projects.search.domain)
    implementation(projects.search.data)
    implementation(projects.search.presentation)

    implementation(projects.cart.domain)
    implementation(projects.cart.data)
    implementation(projects.cart.presentation)

    implementation(projects.checkout.domain)
    implementation(projects.checkout.data)
    implementation(projects.checkout.presentation)

    implementation(projects.profile.domain)
    implementation(projects.profile.data)
    implementation(projects.profile.presentation)

    implementation(projects.order.domain)
    implementation(projects.order.data)
    implementation(projects.order.presentation)
}