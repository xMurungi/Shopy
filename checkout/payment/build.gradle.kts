plugins {
    alias(libs.plugins.shopy.android.feature.ui)
}

android {
    namespace = "com.ag_apps.checkout.payment"
}

dependencies {
    implementation(projects.core.domain)
    implementation (libs.stripe.android)
}