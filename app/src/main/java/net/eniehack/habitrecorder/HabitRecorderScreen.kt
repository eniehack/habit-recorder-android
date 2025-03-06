package net.eniehack.habitrecorder

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import net.eniehack.habitrecorder.ui.EditHabitScreen
import net.eniehack.habitrecorder.ui.EditHabitScreenViewModel
import net.eniehack.habitrecorder.ui.RecordScreen
import net.eniehack.habitrecorder.ui.RecordScreenViewModel

enum class HabitRecorderScreen() {
    HabitRecord,
    EditHabit
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HabitRecorderApp(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = HabitRecorderScreen.HabitRecord.name,
    ) {
        composable(route = HabitRecorderScreen.HabitRecord.name) {
            val viewModel = hiltViewModel<RecordScreenViewModel>()
            val uiState by viewModel.uiState.collectAsState()
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("HabitRecorder") }
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            navController.navigate(HabitRecorderScreen.EditHabit.name)
                        }
                    ) {
                        Icon(Icons.Filled.Add, "add new habits")
                    }
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier.padding(innerPadding),
                ) {
                    RecordScreen(
                        habits = uiState.habits,
                    )
                }
            }
        }
        composable(route = HabitRecorderScreen.EditHabit.name) {
            val viewModel = hiltViewModel<EditHabitScreenViewModel>()
            val uiState by viewModel.uiState.collectAsState()
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("HabitRecorder") }
                    )
                },
            ) { innerPadding ->
                Column(
                    modifier = Modifier.padding(innerPadding),
                ) {
                    EditHabitScreen(
                        title = uiState.title,
                        onTitleChanged = { viewModel.onTitleChanged(it) },
                        onButtonClick = {
                            viewModel.onSubmit()
                            navController.popBackStack(
                                HabitRecorderScreen.HabitRecord.name,
                                inclusive = false
                            )
                        },
                    )
                }
            }
        }
    }
}
