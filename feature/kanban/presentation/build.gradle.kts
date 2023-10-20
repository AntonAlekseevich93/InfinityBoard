plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
}

kotlin {
    sourceSets{
        commonMain {
            dependencies {
                implementation(project(":common:core"))
                implementation(project(":common:models"))
            }
        }
    }
}

android {
    namespace = "ru.infinityboard.infinityboard.kanban.presentation"
}