plugins {
    alias(libs.plugins.shopy.android.library)
}

android {
    namespace = "com.ag_apps.product.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.product.domain)

    implementation(libs.bundles.koin)
}