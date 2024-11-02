plugins {
    alias(libs.plugins.shopy.android.library)
}

android {
    namespace = "com.ag_apps.checkout.data"
}

dependencies {
    implementation(projects.core.domain)

    implementation(projects.checkout.domain)
    implementation(projects.checkout.payment)

    implementation(libs.bundles.koin)
}