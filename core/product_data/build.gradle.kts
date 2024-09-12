plugins {
    alias(libs.plugins.shopy.android.library)
}

android {
    namespace = "com.ag_apps.core.product_data"
}

dependencies {
    implementation(projects.core.domain)
}