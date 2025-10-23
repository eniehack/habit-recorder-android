package net.eniehack.habitrecorder.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.eniehack.habitrecorder.data.Habit
import net.eniehack.habitrecorder.data.HabitType

@Composable
fun EditHabitScreen(
    habit: Habit,
    onDismissHabitTypeSelector: () -> Unit = {},
    onButtonClick: () -> Unit = {},
    onAmountChanged: (Int?) -> Unit = {},
    onTitleChanged: (String) -> Unit = {},
    onUnitChanged: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(15.dp),
    ) {
        TextField(
            value = habit.title,
            onValueChange = { onTitleChanged(it) },
            label = { Text("title") }
        )
        /*
        DropdownMenu(expanded = habitTypeSelectExpand, onDismissRequest = onDismissHabitTypeSelector) {
            DropdownMenuItem(
                text = { Text("achievement") },
                onClick = {},
                leadingIcon = { Icon(Icons.Default.Check, "check icon") }
            )
            DropdownMenuItem(
                text = { Text("record") },
                onClick = {},
                leadingIcon = { Icon(Icons.Default.Edit, "recording icon") }
            )
        }
        TextField(
            value = amount.toString(),
            onValueChange = { onAmountChanged(it.toIntOrNull()) },
            label = { Text("amount") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
         */
        TextField(
            value = habit.unit,
            onValueChange = { onUnitChanged(it) },
            label = { Text("unit") },
        )
        Button(
            onClick = {
                onButtonClick()
            }
        ) {
            Text("Add")
        }
    }
}

@Preview
@Composable
fun EditHabitScreenPreview() {
    EditHabitScreen(
        Habit(
            title = "読書",
            type = HabitType.ACHIEVEMENT,
            unit = "ページ"
        )
    )
}