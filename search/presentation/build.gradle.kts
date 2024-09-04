plugins {
    alias(libs.plugins.shopy.android.feature.ui)
}

android {
    namespace = "com.ag_apps.search.presentation"
}

dependencies {
    implementation(projects.search.domain)
}