import database.LocalKanbanDataSource
import ktor.RemoteKanbanDataSource
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton

val kanbanModule = DI.Module("kanbanModule") {
    bind<KanbanRepository>() with singleton {
        KanbanRepositoryImpl(instance(), instance())
    }

    bind<LocalKanbanDataSource>() with provider {
        LocalKanbanDataSource(instance())
    }

    bind<RemoteKanbanDataSource>() with provider {
        RemoteKanbanDataSource(instance())
    }
}