plugins {
    alias(libs.plugins.shopy.android.library)
}

android {
    namespace = "com.ag_apps.search.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.search.domain)

    implementation(libs.bundles.koin)
}