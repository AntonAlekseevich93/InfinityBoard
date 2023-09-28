plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:core"))
                implementation(project(":common:theme"))
                implementation(project(":common:ui"))
                implementation(project(":feature:kanban:data"))
                implementation(project(":feature:kanban:presentation"))
            }
        }
    }
}

android {
    namespace = "ru.infinityboard.infinityboard.kanban.api"
}
