package net.eniehack.habitrecorder.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.eniehack.habitrecorder.R

@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    userId: String,
    token: String,
    enablePixelaFeature: Boolean = true,
    showDialog: Boolean,
    errorMessage: String? = null,
    onUserIdChanged: (String) -> Unit = {},
    onTokenChanged: (String) -> Unit = {},
    onPixelaFeatureToggled: (Boolean) -> Unit = {},
    onDialogEnabled: () -> Unit = {},
    onConformButtonClicked: () -> Unit = {},
    onDismissButtonClicked: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val uriHandler = LocalUriHandler.current
        val url = "https://pixe.la/"

        ListItem(headlineContent = {
            Text(text = "Pixela設定", style = MaterialTheme.typography.titleSmall)
        })
        ListItem(
            headlineContent = { Text(stringResource(R.string.enable_pixela_button_label)) },
            trailingContent = {
                Switch(checked = enablePixelaFeature, onCheckedChange = onPixelaFeatureToggled)
            }
        )
        ListItem(
            headlineContent = {
                Text(text = stringResource(R.string.pixela_account_settings_label))
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = enablePixelaFeature) { onDialogEnabled() }
        )
        ListItem(
            headlineContent = {
                Text(
                    text = stringResource(R.string.pixelaWebsiteAnchorText),
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = enablePixelaFeature) { uriHandler.openUri(url) }
        )
        ListItem(
            leadingContent = {
                Icon(
                    Icons.Outlined.Info,
                    contentDescription = "information",
                )
            },
            headlineContent = {
                Text(
                    "この機能を有効にすると、習慣の記録データをpixelaに送信します。機能を利用するにはpixelaのアカウントを作成する必要があります。",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        )
        HorizontalDivider(Modifier.padding(vertical = 8.dp), DividerDefaults.Thickness, DividerDefaults.color)
    }
    if (enablePixelaFeature && showDialog) {
        AlertDialog(
            onDismissRequest = { onDismissButtonClicked() },
            title = { Text("pixe.la 認証情報を入力") },
            text = {
                Column {
                    OutlinedTextField(
                        value = userId,
                        onValueChange = onUserIdChanged,
                        label = { Text(stringResource(R.string.pixela_userid_input_label)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = token,
                        onValueChange = onTokenChanged,
                        label = { Text(stringResource(R.string.pixela_token_input_label)) },
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
                Button(onClick = { onConformButtonClicked() }) { Text(stringResource(R.string.save)) }
            },
            dismissButton = {
                TextButton(onClick = { onDismissButtonClicked() }) { Text(stringResource(R.string.cancel)) }
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
