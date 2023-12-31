package database

import sqldelight.com.infinityboard.database.AppDatabase

class LocalKanbanDataSource(dbDriverFactory: DbDriverFactory) {
    private val driver = dbDriverFactory.createDriver()
    private val database = AppDatabase(driver)
    private val dbQuery = database.appDatabaseQueries

    fun test() = "HeLlo Man"
}