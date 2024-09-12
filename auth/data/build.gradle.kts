plugins {
    alias(libs.plugins.shopy.android.library)
}

android {
    namespace = "com.ag_apps.auth.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.auth.domain)

    implementation(libs.bundles.koin)
}