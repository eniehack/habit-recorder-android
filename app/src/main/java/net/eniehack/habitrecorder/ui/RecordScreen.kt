package net.eniehack.habitrecorder.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.eniehack.habitrecorder.R
import net.eniehack.habitrecorder.data.Habit
import net.eniehack.habitrecorder.data.HabitType

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecordScreen(
    habits: List<HabitWithStreak>,
    modifier: Modifier = Modifier,
    onHabitCardChecked: (HabitWithStreak) -> Unit = {},
    onHabitCardClicked: (HabitWithStreak) -> Unit = {},
    onHabitCardEditButtonClicked: (Habit) -> Unit = {},
    onHabitCardLongPressed: (Habit) -> Unit = {},
    isHabitSelected: (Habit) -> Boolean = { false },
) {
    val haptics = LocalHapticFeedback.current
    LazyColumn(
        modifier = modifier.padding(top = 15.dp),
    ) {
        items(items = habits, key = { it.habit.id }) { habit ->
            val backgroundColor = if (isHabitSelected(habit.habit)) {
                Color.LightGray.copy(alpha = 0.5f) // 選択された色
            } else {
                MaterialTheme.colorScheme.surface // 通常の色
            }

            HabitCard(
                habitWithStreak = habit,
                onChecked = onHabitCardChecked,
                onEditButtonClicked = onHabitCardEditButtonClicked,
                modifier = Modifier
                    .background(backgroundColor)
                    .combinedClickable(
                        interactionSource = null,
                        indication = null,
                        onLongClick = {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            onHabitCardLongPressed(habit.habit)
                        },
                        onClick = { onHabitCardClicked(habit) },
                        onLongClickLabel = "onLongClickLabel",
                    )
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
        Column {
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
                    text = pluralStringResource(
                        R.plurals.streaks,
                        habitWithStreak.streaks,
                        habitWithStreak.streaks
                    ),
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
                    type = HabitType.BINARY,
                    unit = "回",
                    lastSyncedAt = null,
                    updatedAt = null
                ),
                streaks = 10,
                hasTodayCheckIn = true,
            ),
            HabitWithStreak(
                habit = Habit(
                    id = 2,
                    title = "読書",
                    type = HabitType.BINARY,
                    unit = "p",
                    lastSyncedAt = null,
                    updatedAt = null
                ),
                streaks = 10
            )
        ),
    )
}