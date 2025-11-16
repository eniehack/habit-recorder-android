package net.eniehack.habitrecorder.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import net.eniehack.habitrecorder.R
import net.eniehack.habitrecorder.data.Habit
import net.eniehack.habitrecorder.data.HabitType

data class HabitWithHistory(
    val habit: Habit,
    val streaks: Int,
    val history: List<Pair<String, Boolean>>
)

@Composable
fun HistoryScreen(
    habits: List<HabitWithHistory>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.padding(top = 15.dp),
    ) {
        items(items = habits, key = { it.habit.id }) { habit ->
            HabitHistoryCard(
                habitWith1WeekHistory = habit,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    }
}

@Composable
private fun ProvideLayoutDirection(
    layoutDirection: LayoutDirection,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalLayoutDirection provides layoutDirection, // < -- ココ!
        content = content,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitHistoryCard(modifier: Modifier = Modifier, habitWith1WeekHistory: HabitWithHistory) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        Text(
            text = habitWith1WeekHistory.habit.title,
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
                    habitWith1WeekHistory.streaks,
                    habitWith1WeekHistory.streaks
                ),
                style = MaterialTheme.typography.bodySmall,
            )
        }
        ProvideLayoutDirection(
            layoutDirection = LayoutDirection.Rtl,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(8.dp)
            ) {
                habitWith1WeekHistory.history.forEach {
                    val description =
                        if (it.second) "${it.first} - done" else "${it.first} - undone"
                    val color =
                        if (it.second) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.3f
                        )
                    val tooltipState = rememberTooltipState()
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                        tooltip = {
                            Text(description)
                        },
                        state = tooltipState
                    ) {
                        Canvas(
                            contentDescription = description,
                            modifier = Modifier.size(24.dp),
                        ) {
                            drawRoundRect(
                                color = color,
                                size = this.size,
                                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun HabitHistoryCardPreview() {
    HabitHistoryCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        habitWith1WeekHistory =
            HabitWithHistory(
                habit = Habit(
                    id = 1,
                    title = "腹筋",
                    type = HabitType.BINARY,
                    unit = "回",
                    lastSyncedAt = null,
                    updatedAt = null
                ),
                streaks = 10,
                history = listOf(
                    Pair("2025-01-01", true),
                    Pair("2025-01-02", false),
                    Pair("2025-01-03", false),
                    Pair("2025-01-04", true),
                    Pair("2025-01-05", false),
                ),
            ),
    )
}
