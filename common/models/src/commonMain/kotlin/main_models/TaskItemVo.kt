package main_models

data class TaskItemVo(
    val id: Int,
    var kanbanColumnId: Int,
    var name: String,
    var description: String = "",
    var positionInColumn: Int = DEFAULT_POSITION_IN_COLUMN,
    var isDone: Boolean = false
)

const val DEFAULT_POSITION_IN_COLUMN = -1