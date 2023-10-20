import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import di.Inject

@Composable
fun Note() {
    val viewModel = remember { Inject.instance<NoteViewModel>() }
    val uiState by viewModel.uiState.collectAsState()
}

@Composable
fun NoteBar(){

}