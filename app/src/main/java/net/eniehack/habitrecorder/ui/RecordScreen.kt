package net.eniehack.habitrecorder.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.eniehack.habitrecorder.R
import net.eniehack.habitrecorder.data.Habit
import net.eniehack.habitrecorder.data.HabitType

@Composable
fun RecordScreen(
    habits: List<HabitWithStreak>,
    onHabitCardChecked: (HabitWithStreak) -> Unit = {},
    onHabitCardEditButtonClicked: (Habit) -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(top = 15.dp),
        //verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        items(items = habits, key = { it.habit.id }) { habit ->
            HabitCard(
                habitWithStreak = habit,
                onChecked = onHabitCardChecked,
                onEditButtonClicked = onHabitCardEditButtonClicked,
            )
        }
    }
}

@Composable
fun HabitCard(
    habitWithStreak: HabitWithStreak,
    onChecked: (HabitWithStreak) -> Unit = {},
    onEditButtonClicked: (Habit) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
    ) {
        Checkbox(
            checked = habitWithStreak.hasTodayCheckIn,
            onCheckedChange = { onChecked(habitWithStreak) }
        )
        Column(
        ) {
            Text(
                text = habitWithStreak.habit.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 2.dp, bottom = 2.dp)
            )
            Row {
                Icon(
                    painterResource(R.drawable.bolt_24px),
                    null,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "${habitWithStreak.streaks} streaks",
                    style = MaterialTheme.typography.bodySmall,
                )
            }

        }
        Spacer(Modifier.weight(1f))
        IconButton(
            onClick = { onEditButtonClicked(habitWithStreak.habit) },
            modifier = Modifier
                .padding(8.dp)
                .size(24.dp)
        ) {
            Icon(Icons.Default.Edit, "Edit habit")
        }
    }
}

@Composable
@Preview
fun RecordHabitsScreenPreview() {
    RecordScreen(
        habits = listOf(
            HabitWithStreak(
                habit = Habit(
                    id = 1,
                    title = "腹筋",
                    type = HabitType.ACHIEVEMENT,
                    unit = "回"
                ),
                streaks = 10,
                hasTodayCheckIn = true,
            ),
            HabitWithStreak(
                habit = Habit(
                    id = 2,
                    title = "読書",
                    type = HabitType.ACHIEVEMENT,
                    unit = "p"
                ),
                streaks = 10
            )
        ),
    )
}