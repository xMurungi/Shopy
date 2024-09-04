plugins {
    alias(libs.plugins.shopy.android.library.compose)
}

android {
    namespace = "com.ag_apps.core.presentation.designsystem"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)

    api(libs.androidx.material.icons.extended)
    api(libs.androidx.compose.material3)
}