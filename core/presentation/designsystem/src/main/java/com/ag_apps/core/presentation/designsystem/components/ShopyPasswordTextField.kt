package com.ag_apps.core.presentation.designsystem.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ag_apps.core.presentation.designsystem.R
import com.ag_apps.core.presentation.designsystem.ShopyTheme

/**
 * @author Ahmed Guedmioui
 */
@Composable
fun ShopyPasswordTextField(
    modifier: Modifier = Modifier,
    textFieldState: TextFieldState,
    isPasswordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    hint: String,
    title: String?
) {

    Column(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.inversePrimary,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(bottom = 16.dp, top = 8.dp)
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            if (title != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.5f),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = title,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.6f)
                    )
                }
            }
        }

        Spacer(Modifier.height(4.dp))

        BasicSecureTextField(
            state = textFieldState,
            textObfuscationMode = if (isPasswordVisible) {
                TextObfuscationMode.Visible
            } else {
                TextObfuscationMode.Hidden
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            textStyle = LocalTextStyle.current.copy(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
            decorator = { innerBox ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        if (textFieldState.text.isEmpty()) {
                            Text(
                                text = hint,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.5f),
                                fontSize = 18.sp,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        innerBox()
                    }


                    Icon(
                        modifier = Modifier
                            .clickable { onTogglePasswordVisibility() },
                        imageVector = if (isPasswordVisible) {
                            Icons.Outlined.Visibility
                        } else {
                            Icons.Outlined.VisibilityOff
                        },
                        contentDescription = if (isPasswordVisible) {
                            stringResource(R.string.show_password)
                        } else {
                            stringResource(R.string.hide_password)
                        },
                        tint = MaterialTheme.colorScheme.onBackground,
                    )

                }
            }
        )
    }
}


@Preview(showSystemUi = true)
@Composable
private fun PasswordTextFieldPreview() {
    ShopyTheme {
        ShopyPasswordTextField(
            modifier = Modifier.fillMaxWidth(),
            textFieldState = TextFieldState(),
            hint = "DsFg13954",
            title = "Password",
            isPasswordVisible = false,
            onTogglePasswordVisibility = {}
        )
    }
}













