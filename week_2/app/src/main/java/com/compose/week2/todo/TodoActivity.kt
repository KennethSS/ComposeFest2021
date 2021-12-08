package com.compose.week2.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.compose.week2.ui.theme.LayoutsCodelabTheme

class TodoActivity : ComponentActivity() {

    private val todoViewModel by viewModels<TodoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LayoutsCodelabTheme {
                Surface {
                    TodoActivityScreen(todoViewModel)
                }
            }
        }
    }

}

@Composable
private fun TodoActivityScreen(todoViewModel: TodoViewModel) {
    val items: List<TodoItem> by todoViewModel.todoItems.observeAsState(listOf())

    TodoScreen(
        items = items,
        onAddItem = { todoViewModel.addItem(it) },
        onRemoveItem = { todoViewModel.removeItem(it) }
    )
}

@Composable
fun TodoRow(todo: TodoItem, onItemClicked: (TodoItem) -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clickable { onItemClicked }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(todo.task)
        Icon(
            imageVector = todo.icon.imageVector,
            contentDescription = stringResource(id = todo.icon.contentDescription)
        )
    }
}