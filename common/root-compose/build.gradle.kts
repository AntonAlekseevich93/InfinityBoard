plugins{
    id("multiplatform-compose-setup")
    id("android-setup")
}

android {
    namespace = "ru.infinityboard.infinityboard"
}

kotlin {
    sourceSets{
        commonMain {
            dependencies {
                api(project(":common:core"))
                implementation(project(":common:theme"))
                implementation(project(":common:di:compose"))
                implementation(project(":feature:kanban:data"))
                implementation(project(":feature:kanban:compose"))
            }
        }
    }
}