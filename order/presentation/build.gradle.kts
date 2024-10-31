plugins {
    alias(libs.plugins.shopy.android.feature.ui)
}

android {
    namespace = "com.ag_apps.order.presentation"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.order.domain)
}