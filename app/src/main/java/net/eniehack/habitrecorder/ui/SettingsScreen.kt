package net.eniehack.habitrecorder.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    userId: String,
    token: String,
    showDialog: Boolean,
    errorMessage: String?= null,
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
                    errorMessage?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
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
