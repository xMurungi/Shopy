plugins {
    alias(libs.plugins.shopy.jvm.library)
}

dependencies {
    implementation(projects.core.domain)
}