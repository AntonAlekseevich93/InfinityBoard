package models

import main_models.TaskItemVo

data class KanbanColumnItem(val id: Int, var taskItems: MutableList<TaskItemVo>)
