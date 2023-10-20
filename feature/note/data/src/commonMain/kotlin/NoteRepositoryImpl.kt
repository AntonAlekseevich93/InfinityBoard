import database.LocalNoteDataSource

class NoteRepositoryImpl(
    private val localNoteDataSource: LocalNoteDataSource
) : NoteRepository {


}