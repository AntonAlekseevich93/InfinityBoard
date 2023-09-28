pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}


rootProject.name = "InfinityBoard"
include(":androidApp")
include(":desktopMain")
include(":common:core")
include(":common:root-ios")
include(":common:root-compose")
include(":common:di:compose")
include(":common:di:ios")
include(":common:theme")
include(":common:ui:drag-and-drop")
include(":feature:kanban:api")
include(":feature:kanban:presentation")
include(":feature:kanban:data")
include(":feature:kanban:compose")
