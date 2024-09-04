plugins {
    alias(libs.plugins.shopy.android.library)
}

android {
    namespace = "com.ag_apps.profile.data"
}

dependencies {
    implementation(projects.core.domain)
}