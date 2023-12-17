plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
}

kotlin {
    sourceSets{
        commonMain {
            dependencies {
                implementation(project(":feature:kanban:presentation"))
                implementation(project(":feature:search:presentation"))
                implementation(project(":feature:search:data")) //todo remove?
                implementation(project(":feature:note:presentation"))
                implementation(project(":feature:note:data")) //todo remove?
                implementation(project(":common:core"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.common.di.compose"
}