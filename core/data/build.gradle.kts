plugins {
    alias(libs.plugins.shopy.android.library)
}

android {
    namespace = "com.ag_apps.core.data"
}

dependencies {
    implementation(libs.timber)
    implementation(libs.koin.android.workmanager)

    implementation(projects.core.domain)
    implementation(projects.core.network)
}