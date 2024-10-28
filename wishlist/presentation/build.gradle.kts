plugins {
    alias(libs.plugins.shopy.android.feature.ui)
}

android {
    namespace = "com.ag_apps.wishlist.presentation"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.wishlist.domain)
}