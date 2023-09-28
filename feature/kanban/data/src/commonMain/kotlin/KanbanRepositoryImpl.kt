import database.LocalKanbanDataSource
import ktor.RemoteKanbanDataSource

class KanbanRepositoryImpl(
    private val remoteKanbanDataSource: RemoteKanbanDataSource,
    private val localKanbanDataSource: LocalKanbanDataSource
) : KanbanRepository {

    override fun test() = localKanbanDataSource.test()
}