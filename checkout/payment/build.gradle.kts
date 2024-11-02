plugins {
    alias(libs.plugins.shopy.android.feature.ui)
    alias(libs.plugins.shopy.jvm.ktor)
}

android {
    namespace = "com.ag_apps.checkout.payment"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.checkout.domain)
    implementation (libs.stripe.android)

    implementation(libs.bundles.koin)
}