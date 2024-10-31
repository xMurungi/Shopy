plugins {
    alias(libs.plugins.shopy.android.feature.ui)
}

android {
    namespace = "com.ag_apps.checkout.presentation"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.checkout.domain)
    implementation(projects.checkout.payment)
}