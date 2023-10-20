plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
}

kotlin {
    sourceSets{
        commonMain {
            dependencies {
                api(project(":feature:note:api"))
                implementation(project(":common:core"))
            }
        }
    }
}


android {
    namespace = "ru.infinityboard.infinityboard.note.data"
}

