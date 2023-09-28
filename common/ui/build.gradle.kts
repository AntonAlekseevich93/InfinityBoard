plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
}

kotlin {
    sourceSets{
        commonMain {
            dependencies {
                api(project(":common:core"))
                implementation(project(":common:theme"))
            }
        }
    }
}

android {
    namespace = "ru.infinityboard.infinityboard.common.ui"
}