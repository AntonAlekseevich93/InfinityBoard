import main_models.TaskItemVo

interface SearchRepository {
    fun searchTasks(searchText: String): List<TaskItemVo>

}