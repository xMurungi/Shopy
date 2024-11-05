plugins {
    alias(libs.plugins.shopy.android.library)
    alias(libs.plugins.shopy.jvm.ktor)
}

android {
    namespace = "com.ag_apps.core.product_data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.data)

    implementation(libs.bundles.koin)
    implementation(libs.timber)
    implementation (libs.gson)

}