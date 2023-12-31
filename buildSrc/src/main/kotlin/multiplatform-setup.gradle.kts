plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("app.cash.sqldelight")
}
//
kotlin{
    jvm("desktop")
    androidTarget()
    ios()

    //можем добавить сюда все зависимости которые должны быть во всех проектах к которым мы подключим этот плагин
}

sqldelight {
    databases.create("AppDatabase") {
        packageName.set("sqldelight.com.infinityboard.database")
    }
    linkSqlite.set(true)
}