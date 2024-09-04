plugins {
    alias(libs.plugins.shopy.android.feature.ui)
}

android {
    namespace = "com.ag_apps.auth.presentation"
}

dependencies {
    implementation(projects.auth.domain)
}