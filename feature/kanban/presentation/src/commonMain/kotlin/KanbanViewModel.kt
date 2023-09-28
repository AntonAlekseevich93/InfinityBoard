import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import models.KanbanColumnItem
import models.TaskItem
import kotlin.random.Random

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

    private val _uiState = MutableStateFlow<MutableList<Section2>>(mutableStateListOf())
    val uiState = _uiState.asStateFlow()

    private val sections =
        mutableStateListOf(
            Section(name = "Section 1"),
            Section(name = "Section 2"),
            Section(name = "Section 3"),
            Section(name = "Section 4"),
            Section(name = "Section 5"),
            Section(name = "Section 6"),
            Section(name = "Section 7"),
            Section(name = "Section 8"),
            Section(name = "Section 9"),
            Section(name = "Section 10"),
//            Section(name = "Section 12"),
//            Section(name = "Section 13"),
//            Section(name = "Section 14"),
//            Section(name = "Section 15"),
//            Section(name = "Section 16"),
//            Section(name = "Section 17"),
        )


    private val sections2 = mutableStateListOf(
        Section(name = "OLO"),
        Section(name = "1"),
        Section(name = "2"),
        Section(name = "3"),
        Section(name = "4"),
        Section(name = "5"),
//    Section(name ="Арг 7"),
//    Section(name ="Арг 8"),
//    Section(name ="Арг 9"),
//    Section(name ="Арг 10"),
    )

    fun taskSwap(from: Int, to: Int, columnIndex: Int, shiftOnly: Boolean) {
        val newList = _uiState.value.toMutableList()

        if (!shiftOnly) {
            val toItem = _uiState.value[columnIndex].sec2[to]
            val fromItem = _uiState.value[columnIndex].sec2[from]
            newList[columnIndex].sec2[from] = toItem
            newList[columnIndex].sec2[to] = fromItem
        } else {
            val fromItem = _uiState.value[columnIndex].sec2[from]
            newList[columnIndex].sec2.removeAt(from)
            newList[columnIndex].sec2.add(to, fromItem)
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

    fun sectionClicked2(it: Section2) {

    }

    fun addNewItemInList(itemIndex: Int, columnIndex: Int, item: Any) {
        (item as? Section)?.let {
            _uiState.value[columnIndex].sec2.add(0, it)
            changeItemPosition(0, itemIndex, columnIndex)
        }
    }

    fun removeItemFromColumn(item: Any, columnIndex: Int) {
        try {
            (item as? Section)?.let {
                _uiState.value[columnIndex].sec2.remove(it)
            }
        } catch (_: Exception) {
        }
    }

    private fun changeItemPosition(fromIndex: Int, toIndex: Int, columnIndex: Int) {
        try {
            val item = _uiState.value[columnIndex].sec2.get(fromIndex)
            _uiState.value[columnIndex].sec2.removeAt(fromIndex)
            _uiState.value[columnIndex].sec2.add(toIndex, item)
        } catch (_: Exception) {
        }
    }

    init {
        _uiState.value = mutableListOf(
            Section2(name = "Section 1", sec2 = sections),
            Section2(name = "Section 2", sec2 = sections2),
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

data class Section2(
    val id: Int = internalId++,
    val name: String = "",
    val description: String = "",
    val color: Long = Random(id).nextLong(),
    val sec2: MutableList<Section>
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