plugins {
    alias(libs.plugins.shopy.android.library)
}

android {
    namespace = "com.ag_apps.checkout.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.checkout.payment)
    implementation(projects.checkout.domain)

    implementation(libs.bundles.koin)
}