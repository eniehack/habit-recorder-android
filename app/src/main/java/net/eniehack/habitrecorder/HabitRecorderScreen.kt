package net.eniehack.habitrecorder

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import net.eniehack.habitrecorder.ui.EditHabitScreen
import net.eniehack.habitrecorder.ui.EditHabitScreenViewModel
import net.eniehack.habitrecorder.ui.RecordScreen
import net.eniehack.habitrecorder.ui.RecordScreenViewModel
import net.eniehack.habitrecorder.ui.SettingScreen
import net.eniehack.habitrecorder.ui.SettingsScreenViewModel

enum class HabitRecorderScreen {
    HabitRecord,
    History,
    Setting,
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
    bottomBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    child: @Composable (Modifier) -> Unit = {},
) {
    Scaffold(
        topBar = { topBar() },
        bottomBar = { bottomBar() },
        floatingActionButton = { floatingActionButton() },
    ) { innerPadding ->
        child(Modifier.padding(innerPadding))
    }
}

data class NavigationItem(
    val name: String,
    val route: HabitRecorderScreen,
    val enabledIcon: ImageVector,
    val disabledIcon: ImageVector,
)

@Composable
fun BottomNavigationBar(
    navController: NavHostController = rememberNavController()
) {
    val items = listOf(
        NavigationItem(
            "record",
            HabitRecorderScreen.HabitRecord,
            Icons.Filled.Edit,
            Icons.Outlined.Edit
        ),
        NavigationItem(
            "history",
            HabitRecorderScreen.History,
            ImageVector.vectorResource(R.drawable.history),
            ImageVector.vectorResource(R.drawable.history)
        ),
        NavigationItem(
            "settings",
            HabitRecorderScreen.Setting,
            Icons.Filled.Settings,
            Icons.Outlined.Settings
        ),
    )
    NavigationBar() {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        item.enabledIcon,
                        contentDescription = item.name,
                    )
                },
                selected = navController.currentDestination?.hierarchy?.any { it.route == item.route.name } == true,
                onClick = { navController.navigate(item.route.name) },
                label = { Text(item.name) }
            )
        }
    }
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

            BackHandler(enabled = uiState.isSelectionMode) {
                viewModel.disableSelectedMode()
            }
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
                topBar = {
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
                                    Toast.makeText(
                                        context,
                                        "deleted $deletedItemLength items",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = "Delete selected items"
                                    )
                                }
                            }
                        )
                    }
                },
                bottomBar = {
                    BottomNavigationBar(
                        navController
                    )
                }
            ) { modifier ->
                RecordScreen(
                    habits = uiState.habits,
                    onHabitCardChecked = { habit ->
                        viewModel.onHabitCardClicked(habit)
                        Toast.makeText(context, "checked in", Toast.LENGTH_SHORT).show()
                    },
                    onHabitCardEditButtonClicked = { it ->
                        navController.navigate(EditHabitNavigationArgument(habitId = it.id))
                    },
                    onHabitCardLongPressed = { it ->
                        viewModel.enableSelectedMode()
                        viewModel.addSelectedItem(it)
                    },
                    isHabitSelected = { it ->
                        uiState.isSelectionMode && uiState.selectedItems.contains(it)
                    },
                    onHabitCardClicked = {
                        Log.d(
                            "HabitBuilder",
                            "mode: ${uiState.isSelectionMode}, target: ${it.habit} items: ${uiState.selectedItems}"
                        )
                        if (uiState.isSelectionMode) {
                            if (uiState.selectedItems.contains(it.habit)) {
                                viewModel.removeSelectedItem(it.habit)
                            } else {
                                viewModel.addSelectedItem(it.habit)
                            }
                        } else {
                            viewModel.onHabitCardClicked(it)
                            Toast.makeText(context, "checked in", Toast.LENGTH_SHORT).show()
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
                    onUnitChanged = { viewModel.onUnitChanged(it) },
                    onButtonClick = {
                        viewModel.onSubmit()
                        Toast.makeText(context, "added", Toast.LENGTH_SHORT).show()
                        navController.popBackStack(
                            HabitRecorderScreen.HabitRecord.name,
                            inclusive = false
                        )
                    },
                    onPixelaIdChanged = {
                        viewModel.onPixelaIdChanged(it)
                    },
                    modifier = modifier,
                )
            }
        }
        composable(route = HabitRecorderScreen.Setting.name) {
            val viewModel = hiltViewModel<SettingsScreenViewModel>()
            val uiState by viewModel.uiState.collectAsState()
            HabitRecorderScaffold(
                bottomBar = {
                    BottomNavigationBar(
                        navController
                    )
                }
            ) { modifier ->
                SettingScreen(
                    modifier,
                    uiState.pixelaUserId,
                    uiState.pixelaToken,
                    showDialog = uiState.showPixelaCredentialDialog,
                    onUserIdChanged = { viewModel.onPixelaUserIdChanged(it) },
                    onTokenChanged = { viewModel.onPixelaTokenChanged(it) },
                    onDialogEnabled = { viewModel.togglePixelaCredentialDialog() },
                    onConformButtonClicked = { viewModel.savePixelaCredential(); viewModel.togglePixelaCredentialDialog() },
                    onDismissButtonClicked = { viewModel.togglePixelaCredentialDialog() }
                )
            }
        }
    }
}
