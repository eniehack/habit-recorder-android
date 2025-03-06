package net.eniehack.habitrecorder.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun EditHabitScreen(
    title: String = "",
    onButtonClick: () -> Unit = {},
    onTitleChanged: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        TextField(title, onValueChange = { onTitleChanged(it) }, label = { Text("title") })
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
    EditHabitScreen()
}