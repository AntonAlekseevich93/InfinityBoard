plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
}

kotlin {
    sourceSets{
        commonMain {
            dependencies {
                implementation(project(":feature:kanban:presentation"))
                implementation(project(":common:core"))
            }
        }
    }
}

android {
    namespace = "ru.infinityboard.infinityboard.common.di.compose"
}