package net.eniehack.habitrecorder.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.eniehack.habitrecorder.R
import net.eniehack.habitrecorder.data.Habit
import net.eniehack.habitrecorder.data.HabitType

@Composable
fun EditHabitScreen(
    habit: Habit,
    enabledPixelaIntegration: Boolean = true,
    @StringRes submitButtonLabel: Int = R.string.add_habit_submit_button_label,
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
            label = { Text("title") },
            placeholder = { Text("50ページ読書する") },
            supportingText = { Text("「今日はこの習慣を達成できた」と判断できる明確な目標を入力してください。") }
        )
        TextField(
            value = habit.unit,
            onValueChange = { onUnitChanged(it) },
            label = { Text("unit") },
            singleLine = true,
        )
        TextField(
            value = habit.pixelaId ?: "",
            enabled = enabledPixelaIntegration,
            onValueChange = { onPixelaIdChanged(it) },
            label = { Text("pixela id") },
            supportingText = { Text("Pixelaに登録する場合、あらかじめ登録したidを入力してください。") }
        )
        Button(
            onClick = {
                onButtonClick()
            }
        ) {
            Text(text = stringResource(submitButtonLabel))
        }
    }
}

@Preview
@Composable
fun EditHabitScreenPreview() {
    EditHabitScreen(
        Habit(
            title = "読書",
            type = HabitType.BINARY,
            unit = "ページ",
            pixelaId = null,
            lastSyncedAt = null,
            updatedAt = null,
        )
    )
}