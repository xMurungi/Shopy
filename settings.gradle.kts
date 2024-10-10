pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Shopy"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(":auth:data")
include(":auth:domain")
include(":auth:presentation")
include(":core:presentation:designsystem")
include(":core:presentation:ui")
include(":core:domain")
include(":core:data")
include(":core:user_data")
include(":core:product_data")
include(":search:data")
include(":search:presentation")
include(":search:domain")
include(":cart:data")
include(":cart:presentation")
include(":cart:domain")
include(":checkout:data")
include(":checkout:payment")
include(":checkout:presentation")
include(":checkout:domain")
include(":category:presentation")
include(":wishlist:data")
include(":wishlist:presentation")
include(":wishlist:domain")
include(":category:data")
include(":category:domain")
include(":profile:data")
include(":profile:presentation")
include(":profile:domain")
include(":order:data")
include(":order:presentation")
include(":order:domain")
include(":product:data")
include(":product:domain")
include(":product:presentation")
