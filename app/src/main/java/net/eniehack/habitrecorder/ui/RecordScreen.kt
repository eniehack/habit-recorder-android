package net.eniehack.habitrecorder.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.eniehack.habitrecorder.data.Habit

@Composable
fun RecordScreen(
    habits: List<Habit>,
    modifier: Modifier = Modifier
) {
    HabitsColumn(
        habits = habits,
        modifier = modifier.padding(15.dp),
    )
}

@Composable
fun HabitsColumn(
    habits: List<Habit>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        items(items = habits, key = { it.id }) { habit ->
            HabitCard(habit = habit, modifier = modifier)
        }
    }
}

@Composable
fun HabitCard(
    habit: Habit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) { 
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = modifier.padding(16.dp)
            ) {
            Text(habit.title)
        }
    }
}

@Composable
@Preview
fun RecordHabitsScreenPreview() {
    RecordScreen(habits = listOf(
        Habit(
            id = 1,
            title = "腹筋",
            pixelaId = null
        ),
        Habit(
            id = 2,
            title = "読書",
            pixelaId = null
        )
    ))
}