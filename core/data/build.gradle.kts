plugins {
    alias(libs.plugins.shopy.android.library)
    alias(libs.plugins.shopy.jvm.ktor)
}

android {
    namespace = "com.ag_apps.core.data"
}

dependencies {
    implementation(libs.timber)
    implementation(libs.koin.android.workmanager)

    implementation(projects.core.domain)
}