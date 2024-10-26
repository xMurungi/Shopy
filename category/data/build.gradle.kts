plugins {
    alias(libs.plugins.shopy.android.library)
}

android {
    namespace = "com.ag_apps.category.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.category.domain)

    implementation(libs.bundles.koin)
}