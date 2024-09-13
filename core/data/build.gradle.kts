plugins {
    alias(libs.plugins.shopy.android.library)
    alias(libs.plugins.shopy.kotlinx.serialization)
}

android {
    namespace = "com.ag_apps.core.data"
}

dependencies {
    implementation(libs.timber)
    implementation(libs.bundles.koin)

    implementation(projects.core.domain)
}