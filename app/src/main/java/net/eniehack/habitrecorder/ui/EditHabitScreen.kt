package net.eniehack.habitrecorder.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
    onButtonClick: () -> Unit = {},
    onPixelaIdChanged: (String?) -> Unit = {},
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
        TextField(
            value = habit.unit,
            onValueChange = { onUnitChanged(it) },
            label = { Text("unit") },
        )
        TextField(
            value = habit.pixelaId ?: "",
            onValueChange = { onPixelaIdChanged(it) },
            label = { Text("pixela id") }
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