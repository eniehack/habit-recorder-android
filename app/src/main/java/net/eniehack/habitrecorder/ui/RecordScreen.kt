package net.eniehack.habitrecorder.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.eniehack.habitrecorder.R

@Composable
fun RecordScreen(
    habits: List<HabitWithStreak>,
    onHabitCardButtonClicked: (HabitWithStreak) -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        items(items = habits, key = { it.id }) { habit ->
            HabitCard(
                habit = habit,
                onAddButtonClicked = onHabitCardButtonClicked,
            )
        }
    }
}

@Composable
fun HabitCard(
    habit: HabitWithStreak,
    onAddButtonClicked: (HabitWithStreak) -> Unit = {},
    onHabitCardClicked: (HabitWithStreak) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { onHabitCardClicked(habit) },
        modifier = modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .fillMaxSize()
                .padding(15.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = habit.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Row {
                    Icon(
                        painterResource(R.drawable.bolt_24px),
                        null,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "${habit.streaks} streaks",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                Row (
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = modifier.fillMaxWidth()
                        .padding(8.dp)
                        .size(24.dp)
                ){
                    IconButton(
                        onClick = { onAddButtonClicked(habit) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Done, "create check in instantly")
                    }
                    IconButton(
                        onClick = {  },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Edit, "create check in")
                    }
                }
            }
            Box(modifier.weight(1f))
        }
    }
}

@Composable
@Preview
fun RecordHabitsScreenPreview() {
    RecordScreen(
        habits = listOf(
            HabitWithStreak(
                id = 1,
                title = "腹筋",
                unit = "回",
                streaks = 10
            ),
            HabitWithStreak(
                id = 2,
                title = "読書",
                unit = "p",
                streaks = 10
            )
        ),
    )
}