import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

val viewModelsModule = DI.Module("viewModelsModule") {
    bind<KanbanViewModel>() with singleton {
        KanbanViewModel()
    }
}