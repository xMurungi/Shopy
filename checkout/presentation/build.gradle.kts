plugins {
    alias(libs.plugins.shopy.android.feature.ui)
}

android {
    namespace = "com.ag_apps.checkout.presentation"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}