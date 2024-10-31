plugins {
    alias(libs.plugins.shopy.android.library)
}

android {
    namespace = "com.ag_apps.order.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.order.domain)

    implementation(libs.bundles.koin)
}