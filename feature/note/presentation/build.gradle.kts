plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
}

kotlin {
    sourceSets{
        commonMain {
            dependencies {
                implementation(project(":common:core"))
                implementation(project(":feature:note:api"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.note.presentation"
}