plugins {
    alias(libs.plugins.shopy.android.library)
    alias(libs.plugins.shopy.firebase)
}

android {
    namespace = "com.ag_apps.core.user_data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(libs.bundles.koin)
    implementation(libs.timber)
    implementation (libs.kotlin.reflect)
}