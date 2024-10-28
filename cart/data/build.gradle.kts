plugins {
    alias(libs.plugins.shopy.android.library)
}

android {
    namespace = "com.ag_apps.cart.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.cart.domain)

    implementation(libs.bundles.koin)
}