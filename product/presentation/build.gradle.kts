plugins {
    alias(libs.plugins.shopy.android.feature.ui)
}

android {
    namespace = "com.ag_apps.product.presentation"
}

dependencies {
    implementation(projects.product.domain)
}