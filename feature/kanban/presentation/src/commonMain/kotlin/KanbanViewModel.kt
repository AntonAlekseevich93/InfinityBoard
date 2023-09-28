import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import models.KanbanColumnItem
import models.TaskItem

class KanbanViewModel {
    val first = KanbanColumnItem(
        id = 0,
        taskItems =
        mutableStateListOf(
            TaskItem(1, 0, "Помыть посуду"),
            TaskItem(5, 0, "ВыросШирос"),
            TaskItem(4, 0, "Экипировка")
        )
    )
    val sencond = KanbanColumnItem(
        id = 1,
        taskItems = mutableStateListOf(TaskItem(2, 1, "Приготовить завтрак"))
    )

    val third = KanbanColumnItem(
        id = 2,
        taskItems =
        mutableStateListOf<TaskItem>(TaskItem(3, 2, "Убраться"))

    )
    private val _kanbanList = MutableStateFlow(
        mutableStateListOf<KanbanColumnItem>(
            first,
            sencond,
            third,
        )
    )
    var kanbanList = _kanbanList.asStateFlow()

    var isCurrentlyDragging = MutableStateFlow(false)
        private set

    var items = MutableStateFlow(emptyList<PersonUiItem>())
        private set

    var addedPersons = mutableStateListOf<PersonUiItem>()
        private set

    init {
        items.value = mutableListOf(
            PersonUiItem("Michael", "1", Color.Gray),
            PersonUiItem("Larissa", "2", Color.Blue),
            PersonUiItem("Marc", "3", Color.Green),
        )
    }

    fun startDragging() {
        isCurrentlyDragging.value = true
    }

    fun stopDragging() {
        isCurrentlyDragging.value = false
    }

    fun addPerson(personUiItem: PersonUiItem) {
        println("Added Person")
        addedPersons.add(personUiItem)
    }

    fun deleteItemById(item: TaskItem, newId: Int, newIndex: Int) {
        _kanbanList.value.forEachIndexed { index, kanbanColumnItem ->
            val count = kanbanColumnItem.taskItems.count { it.id == item.id }
            if (count > 0) {
                println("viewModel ${_kanbanList.value[index].taskItems.joinToString { it.name }}")
                _kanbanList.value[index].taskItems.removeAll { it.id == item.id }
                println("AFTER viewModel ${_kanbanList.value[index].taskItems.joinToString { it.name }}")
            }
        }
        _kanbanList.value[newIndex].taskItems.add(item.copy(kanbanColumnId = newId))
    }

}

data class PersonUiItem(
    val name: String,
    val id: String,
    val backgroundColor: Color
)