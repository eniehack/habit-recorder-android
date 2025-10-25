package net.eniehack.habitrecorder

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import net.eniehack.habitrecorder.ui.EditHabitScreen
import net.eniehack.habitrecorder.ui.EditHabitScreenViewModel
import net.eniehack.habitrecorder.ui.RecordScreen
import net.eniehack.habitrecorder.ui.RecordScreenViewModel

enum class HabitRecorderScreen {
    HabitRecord,
    EditHabit
}

@Serializable
data class EditHabitNavigationArgument(
    val habitId: Int?
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitRecorderScaffold(
    topBar: @Composable () -> Unit = {
        TopAppBar(
            title = { Text("HabitRecorder") }
        )
    },
    floatingActionButton: @Composable () -> Unit = {},
    child: @Composable (Modifier) -> Unit = {},
) {
    Scaffold(
        topBar = { topBar() },
        floatingActionButton = { floatingActionButton() },
        bottomBar = {
        }
    ) { innerPadding ->
        child(Modifier.padding(innerPadding))
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController = rememberNavController()
) {
    val items = listOf(Pair("record", Icons.Default.Edit), Pair("analytics", R.drawable.monitoring_24px))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
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
            val context = LocalContext.current
            viewModel.getAllHabits()
            Log.d("HabitRecorderApp", uiState.habits.toString())
            HabitRecorderScaffold(
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            navController.navigate(EditHabitNavigationArgument(habitId = null))
                        }
                    ) {
                        Icon(Icons.Filled.Add, "add new habits")
                    }
                },
                topBar ={
                    if (uiState.isSelectionMode) {
                        TopAppBar(
                            title = { Text("選択") },
                            navigationIcon = {
                                IconButton(onClick = {
                                    viewModel.disableSelectedMode()
                                }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Localized description"
                                    )
                                }
                            },
                            actions = {
                                IconButton(onClick = {
                                    val deletedItemLength = uiState.selectedItems.size
                                    viewModel.removeSelectedItemsFromDatabase()
                                    viewModel.disableSelectedMode()
                                    Toast.makeText(context, "deleted $deletedItemLength items", Toast.LENGTH_SHORT).show()
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = "Delete selected items"
                                    )
                                }
                            }
                        )
                    }
                }
            ) { modifier ->
                RecordScreen(
                    habits = uiState.habits,
                    onHabitCardChecked = { habit ->
                        viewModel.onHabitCardClicked(habit)
                        Toast.makeText(context, "checked in", Toast.LENGTH_SHORT).show()
                    },
                    onHabitCardEditButtonClicked = { habit ->
                        navController.navigate(EditHabitNavigationArgument(habitId = habit.id))
                    },
                    onHabitCardLongPressed = { habit ->
                        viewModel.enableSelectedMode()
                        viewModel.addSelectedItem(habit)
                    },
                    isHabitSelected = { habit ->
                        uiState.isSelectionMode && uiState.selectedItems.contains(habit)
                    },
                    onHabitCardSelected = { habit ->
                        if (uiState.isSelectionMode) {
                            viewModel.addSelectedItem(habit)
                        }
                    },
                    modifier = modifier,
                )
            }
        }
        composable<EditHabitNavigationArgument> {
            val viewModel = hiltViewModel<EditHabitScreenViewModel>()
            val uiState by viewModel.uiState.collectAsState()
            val context = LocalContext.current
            HabitRecorderScaffold { modifier ->
                EditHabitScreen(
                    habit = uiState.habit,
                    onTitleChanged = { viewModel.onTitleChanged(it) },
                    //onAmountChanged = { viewModel.onAmountChanged(it) },
                    onUnitChanged = { viewModel.onUnitChanged(it) },
                    onButtonClick = {
                        viewModel.onSubmit()
                        Toast.makeText(context, "added", Toast.LENGTH_SHORT).show()
                        navController.popBackStack(
                            HabitRecorderScreen.HabitRecord.name,
                            inclusive = false
                        )
                    },
                    modifier = modifier,
                )
            }
        }
    }
}
