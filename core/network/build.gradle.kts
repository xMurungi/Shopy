plugins {
    alias(libs.plugins.shopy.android.library)
    alias(libs.plugins.shopy.jvm.ktor)
}

android {
    namespace = "com.ag_apps.core.network"
}

dependencies {
    implementation(libs.org.mongodb.bson)

    implementation(projects.core.domain)
}