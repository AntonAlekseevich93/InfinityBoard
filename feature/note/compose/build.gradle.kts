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
                implementation(project(":common:resources:drawable"))
                implementation(project(":common:resources:strings"))
                implementation(project(":feature:note:api"))
                implementation(project(":feature:note:presentation"))
            }
        }
    }
}

android {
    namespace = "ru.infinityboard.infinityboard.note.api"
}
