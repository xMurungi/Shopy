plugins {
    alias(libs.plugins.shopy.android.feature.ui)
}

android {
    namespace = "com.ag_apps.profile.presentation"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.profile.domain)
}