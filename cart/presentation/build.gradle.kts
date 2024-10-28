plugins {
    alias(libs.plugins.shopy.android.feature.ui)
}

android {
    namespace = "com.ag_apps.cart.presentation"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.cart.domain)
}