plugins {
    alias(libs.plugins.shopy.android.library)
    alias(libs.plugins.shopy.kotlinx.serialization)
    alias(libs.plugins.shopy.jvm.ktor)
}

android {
    namespace = "com.ag_apps.core.data"
}

dependencies {
    implementation(libs.timber)
    implementation(libs.bundles.koin)
    implementation(libs.androidx.security.crypto.ktx)

    implementation(projects.core.domain)
}