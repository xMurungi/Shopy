plugins {
    alias(libs.plugins.shopy.android.library)
}

android {
    namespace = "com.ag_apps.core.network"
}

dependencies {

    implementation (libs.firebase.auth.ktx)
    implementation (libs.play.services.auth)

    implementation(projects.core.domain)
}