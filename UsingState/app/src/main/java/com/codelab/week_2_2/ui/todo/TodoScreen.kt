package com.codelab.week_2_2.ui.todo

import android.widget.Space
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.compose.week2.ui.padding

@Composable
fun TodoScreen(
    items: List<TodoItem>,
    currentlyEditing: TodoItem?,
    onAddItem: (TodoItem) -> Unit,
    onRemoveItem: (TodoItem) -> Unit,
    onStartEdit: (TodoItem) -> Unit,
    onEditItemChange: (TodoItem) -> Unit,
    onEditDone: () -> Unit
) {

    Column {
        TodoItemEntryInput( onItemComplete = { } )
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(top = 8.dp)
        ) {
            items(items = items) { todo ->
                if (currentlyEditing?.id == todo.id) {
                    TodoItemInlineEditor(
                        item = currentlyEditing,
                        onEditItemChange = onEditItemChange,
                        onEditDone = { onRemoveItem(todo) },
                        onRemoveItem = { onRemoveItem(todo) }
                    )
                } else {
                    TodoRow(
                        todo = todo,
                        onItemClicked = { onRemoveItem(it) },
                        modifier = Modifier.fillParentMaxWidth()
                    )
                }
            }
        }

        Button(
            onClick = { onAddItem(generateRandomTodoItem()) },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text("Add Random Item")
        }
    }
}


@Composable
fun TodoItemInlineEditor(
    item: TodoItem,
    onEditItemChange: (TodoItem) -> Unit,
    onEditDone: () -> Unit,
    onRemoveItem: () -> Unit
) = TodoItemInput(
    text = item.task,
    onTextChange = { onEditItemChange(item.copy(task = it)) },
    icon = item.icon,
    onIconChange = { onEditItemChange(item.copy(icon = it)) },
    submit = onEditDone,
    iconsVisible = true,
    buttonSlot = {
        Row {
            val shrinkButtons = Modifier.widthIn(20.dp)

            TextButton(onClick = onEditDone, modifier = shrinkButtons) {
                Text(
                    text = "\uD83D\uDCBE",
                    textAlign = TextAlign.End,
                    modifier = Modifier.widthIn(30.dp)
                )
            }

            TextButton(onClick = onRemoveItem, modifier = shrinkButtons) {
                Text(
                    text = "❌",
                    textAlign = TextAlign.End,
                    modifier = Modifier.width(30.dp)
                )
            }
        }
    }
)

@Composable
fun TodoInputTextField(text: String, onTextChange: (String) -> Unit, modifier: Modifier) {
    TodoInputText(text, onTextChange, modifier)
}

@Composable
fun TodoItemInput(text: String,
                  onTextChange: (String) -> Unit,
                  icon: TodoIcon,
                  onIconChange: (TodoIcon) -> Unit,
                  submit: () -> Unit,
                  iconsVisible: Boolean,
                  buttonSlot: @Composable () -> Unit) {


    Column {
        Row(
            Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        ) {
            TodoInputTextField(
                text = text,
                onTextChange = onTextChange,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            TodoEditButton(
                onClick = submit,
                text = "Add",
                modifier = Modifier.align(Alignment.CenterVertically),
                enabled = text.isNotBlank()
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.align(Alignment.CenterVertically)) { buttonSlot() }
        }
        if (iconsVisible) {
            AnimatedIconRow(icon, onIconChange, Modifier.padding(top = 8.dp))
        } else {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun TodoEditButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
   TextButton(
       onClick = onClick,
       shape = CircleShape,
       enabled = enabled,
       modifier = modifier
   ) {
       Text(text)
   }
}

@Composable
fun TodoItemEntryInput(onItemComplete: (TodoItem) -> Unit) {
    val (text, setText) = remember { mutableStateOf("") }
    val (icon, setIcon) = remember { mutableStateOf(TodoIcon.Default) }

    val submit = {
        onItemComplete(TodoItem(text, icon))
        setIcon(TodoIcon.Default)
        setText("")
    }

    val iconsVisible = text.isNotBlank()

    TodoItemInput(
        text = text,
        onTextChange = setText,
        icon = icon,
        onIconChange = setIcon,
        submit = submit,
        iconsVisible = iconsVisible
    ) {
        TodoEditButton(onClick = submit, text ="Add", enabled = text.isNotBlank())
    }
}