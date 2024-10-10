plugins {
    alias(libs.plugins.shopy.android.library)
}

android {
    namespace = "com.ag_apps.search.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.userData)
    implementation(projects.search.domain)
}