plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
}

kotlin {
    sourceSets{
        commonMain {
            dependencies {
                api(project(":feature:kanban:api"))
                api(project(":feature:kanban:api"))
                implementation(project(":common:core"))
            }
        }
    }
}


android {
    namespace = "ru.yourlibrary.yourlibrary.kanban.data"
}
dependencies {
    implementation(project(mapOf("path" to ":common:ui")))
}
