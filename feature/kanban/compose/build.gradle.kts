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
                implementation(project(":common:models"))
                implementation(project(":common:resources:drawable"))
                implementation(project(":feature:kanban:data"))
                implementation(project(":feature:kanban:presentation"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.kanban.api"
}
