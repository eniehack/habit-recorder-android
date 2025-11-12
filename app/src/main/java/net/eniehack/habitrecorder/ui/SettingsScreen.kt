package net.eniehack.habitrecorder.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

data class PixelaSettings(
    val userId: String = "",
    val apiKey: String = "",
)

@Composable
fun PixelaSettingApiKeyDialog(
    apiKey: String,
    modifier: Modifier = Modifier,
    onApiKeyChanged: (String) -> Unit = {},
) {
    TextField(
        value = apiKey,
        onValueChange = { onApiKeyChanged(it) },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
        ),
        label = { Text("API Key") },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PixelaUserIdSettingDialog(
    userId: String,
    modifier: Modifier = Modifier,
    onUserIdChanged: (String) -> Unit = {},
) {
    BasicAlertDialog(
        onDismissRequest = TODO(),
        modifier = modifier,
        properties = DialogProperties()
    ) {
        TextField(
            value = userId,
            onValueChange = { onUserIdChanged(it) },
            label = { Text("user id") }
        )
    }
}

@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    userId: String,
    token: String,
    showDialog: Boolean,
    onUserIdChanged: (String) -> Unit = {},
    onTokenChanged: (String) -> Unit = {},
    onDialogEnabled: () -> Unit = {},
    onConformButtonClicked: () -> Unit = {},
    onDismissButtonClicked: () -> Unit = {},
) {
    Column(modifier = modifier.padding(8.dp)) {
        Text(text = "pixe.la設定", style = MaterialTheme.typography.titleSmall)
        Text(text = "認証情報", modifier = Modifier.fillMaxWidth().padding(16.dp).clickable{ onDialogEnabled() })
        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismissButtonClicked() },
            title = { Text("ユーザー認証情報を編集") },
            text = {
                Column {
                    OutlinedTextField(
                        value = userId,
                        onValueChange = onUserIdChanged,
                        label = { Text("ユーザーID") },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = token,
                        onValueChange = onTokenChanged,
                        label = { Text("パスワード") },
                        visualTransformation = PasswordVisualTransformation(), // 入力を非表示
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done,
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { onConformButtonClicked() }) { Text("保存") }
            },
            dismissButton = {
                TextButton(onClick = { onDismissButtonClicked() }) { Text("キャンセル") }
            }
        )
    }
}

@Preview
@Composable
fun TestSettingScreen() {
    SettingScreen(
        userId = "foobar",
        token = "token",
        showDialog = false,
    )
}
