plugins {
    alias(libs.plugins.shopy.android.library)
    alias(libs.plugins.shopy.kotlinx.serialization)
    alias(libs.plugins.shopy.firebase)
}

android {
    namespace = "com.ag_apps.auth.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.auth.domain)

    implementation(libs.bundles.koin)

    implementation(libs.timber)
}