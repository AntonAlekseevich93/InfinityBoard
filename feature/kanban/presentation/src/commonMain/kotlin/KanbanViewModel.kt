import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import main_models.TaskItemVo
import models.KanbanColumnItem
import kotlin.random.Random

class KanbanViewModel {
    val first = KanbanColumnItem(
        id = 0,
        taskItems =
        mutableStateListOf(
            TaskItemVo(1, 0, "Помыть посуду"),
            TaskItemVo(5, 0, "ВыросШирос"),
            TaskItemVo(4, 0, "Экипировка")
        )
    )
    val sencond = KanbanColumnItem(
        id = 1,
        taskItems = mutableStateListOf(TaskItemVo(2, 1, "Приготовить завтрак"))
    )

    val third = KanbanColumnItem(
        id = 2,
        taskItems =
        mutableStateListOf<TaskItemVo>(TaskItemVo(3, 2, "Убраться"))

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

    fun deleteItemById(item: TaskItemVo, newId: Int, newIndex: Int) {
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

    private val _uiState = MutableStateFlow<MutableList<KanbanBoard>>(mutableStateListOf())
    val uiState = _uiState.asStateFlow()

    private val sections =
        mutableStateListOf(
            TaskItemVo(id = 0, kanbanColumnId = 0, name = "Задача 1"),
            TaskItemVo(id = 1, kanbanColumnId = 0, name = "Задача 2"),
            TaskItemVo(id = 2, kanbanColumnId = 0, name = "Задача 3"),
            TaskItemVo(id = 3, kanbanColumnId = 0, name = "Задача 4"),
            TaskItemVo(id = 4, kanbanColumnId = 0, name = "Задача 5"),
        )


    private val sections2 = mutableStateListOf(
        TaskItemVo(id = 0, kanbanColumnId = 0, name = "Кто-то сделать 1"),
        TaskItemVo(id = 1, kanbanColumnId = 0, name = "Кто-то сделать 2"),
        TaskItemVo(id = 2, kanbanColumnId = 0, name = "Кто-то сделать 3"),
        TaskItemVo(id = 3, kanbanColumnId = 0, name = "Кто-то сделать 4"),
        TaskItemVo(id = 4, kanbanColumnId = 0, name = "Кто-то сделать 5"),
    )

    fun taskSwap(from: Int, to: Int, columnIndex: Int, shiftOnly: Boolean) {
        val newList = _uiState.value.toMutableList()

        if (!shiftOnly) {
            val toItem = _uiState.value[columnIndex].taskList[to]
            val fromItem = _uiState.value[columnIndex].taskList[from]
            newList[columnIndex].taskList[from] = toItem
            newList[columnIndex].taskList[to] = fromItem
        } else {
            val fromItem = _uiState.value[columnIndex].taskList[from]
            newList[columnIndex].taskList.removeAt(from)
            newList[columnIndex].taskList.add(to, fromItem)
        }
        _uiState.value = newList
    }

    fun swapSections2(from: Int, to: Int) {
        val fromItem = _uiState.value[from]
        val toItem = _uiState.value[to]
        val newList = _uiState.value.toMutableList()
        newList[from] = toItem
        newList[to] = fromItem

        _uiState.value = newList
    }

    fun sectionClicked(item: Section) {
        println("Clicked $item")
    }

    fun sectionClicked2(it: KanbanBoard) {

    }

    fun addNewItemInList(itemIndex: Int, columnIndex: Int, item: Any) {
        (item as? TaskItemVo)?.let {
            _uiState.value[columnIndex].taskList.add(0, it)
            changeItemPosition(0, itemIndex, columnIndex)
        }
    }

    fun removeItemFromColumn(item: Any, columnIndex: Int) {
        try {
            (item as? TaskItemVo)?.let {
                _uiState.value[columnIndex].taskList.remove(it)
            }
        } catch (_: Exception) {
        }
    }

    private fun changeItemPosition(fromIndex: Int, toIndex: Int, columnIndex: Int) {
        try {
            val item = _uiState.value[columnIndex].taskList.get(fromIndex)
            _uiState.value[columnIndex].taskList.removeAt(fromIndex)
            _uiState.value[columnIndex].taskList.add(toIndex, item)
        } catch (_: Exception) {
        }
    }

    init {
        _uiState.value = mutableListOf(
            KanbanBoard(name = "Section 1", taskList = sections),
            KanbanBoard(name = "Section 2", taskList = sections2),
        )
    }

}


data class Section(
    val id: Int = internalId++,
    val name: String = "",
    val description: String = "",
    val color: Long = Random(id).nextLong()
) {
    companion object {
        private var internalId = 0
    }
}

data class KanbanBoard( //todo переименовать в нормальное название
    val id: Int = internalId++,
    val name: String = "",
    val description: String = "",
    val color: Long = Random(id).nextLong(),
    val taskList: MutableList<TaskItemVo>
) {
    companion object {
        private var internalId = 0
    }
}

data class PersonUiItem(
    val name: String,
    val id: String,
    val backgroundColor: Color
)