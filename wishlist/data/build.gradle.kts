plugins {
    alias(libs.plugins.shopy.android.library)
}

android {
    namespace = "com.ag_apps.wishlist.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.wishlist.domain)

    implementation(libs.bundles.koin)
}