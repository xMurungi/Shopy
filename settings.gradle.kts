pluginManagement {
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
include(":app")
include(":auth:data")
include(":auth:domain")
include(":auth:presentation")
include(":core:presentation:designsystem")
include(":core:presentation:ui")
include(":core:domain")
include(":core:data")
include(":core:network")
include(":search:data")
include(":search:presentation")
include(":search:domain")
include(":cart:data")
include(":cart:presntation")
include(":cart:domain")
include(":checkout:data")
include(":checkout:payment")
include(":checkout:presntation")
include(":checkout:domain")
include(":wishlist:data")
include(":wishlist:presntation")
include(":wishlist:domain")
include(":category:data")
include(":category:presntation")
include(":category:domain")
include(":profile:data")
include(":profile:presntaion")
include(":profile:domain")
include(":order:data")
include(":order:prentation")
include(":order:domain")
include(":product:data")
include(":product:prenstation")
include(":product:domain")
