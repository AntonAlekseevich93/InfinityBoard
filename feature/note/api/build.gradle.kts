plugins {
    id("multiplatform-setup")
    id("android-setup")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
            }
        }
    }
}

android {
    namespace = "ru.infinityboard.infinityboard.note.api"
}