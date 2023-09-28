import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Application() {
    AppTheme {
        Column(modifier = Modifier.fillMaxSize()) {
//            KanbanBoard()
            StuffListUI()
        }
    }
}

