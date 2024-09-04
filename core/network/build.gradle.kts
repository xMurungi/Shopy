plugins {
    alias(libs.plugins.shopy.android.library)
}

android {
    namespace = "com.ag_apps.core.network"
}

dependencies {
    implementation(libs.org.mongodb.bson)

    implementation(projects.core.domain)
}