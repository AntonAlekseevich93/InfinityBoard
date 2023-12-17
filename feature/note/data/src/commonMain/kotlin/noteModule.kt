
import database.LocalNoteDataSource
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton

val noteModule = DI.Module("noteModule") {
    bind<NoteRepository>() with singleton {
        NoteRepositoryImpl(instance())
    }

    bind<LocalNoteDataSource>() with provider {
        LocalNoteDataSource(instance())
    }
}