plugins {
    alias(libs.plugins.shopy.android.feature.ui)
}

android {
    namespace = "com.ag_apps.category.presentation"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.category.domain)
}