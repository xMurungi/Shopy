plugins {
    alias(libs.plugins.shopy.android.library)
    alias(libs.plugins.shopy.firebase)
}

android {
    namespace = "com.ag_apps.core.network"
}

dependencies {
    implementation(projects.core.domain)
    implementation(libs.bundles.koin)
    implementation(libs.timber)
}