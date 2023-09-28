package models

data class KanbanColumnItem(val id: Int, var taskItems: MutableList<TaskItem>)
