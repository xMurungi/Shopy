package com.ag_apps.auth.presentation.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowRightAlt
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ag_apps.auth.presentation.R
import com.ag_apps.core.presentation.designsystem.Poppins
import com.ag_apps.core.presentation.designsystem.ShopyTheme
import com.ag_apps.core.presentation.designsystem.components.ShopyButton
import com.ag_apps.core.presentation.designsystem.components.OutlinedButton
import com.ag_apps.core.presentation.designsystem.components.ShopyLargeTopBar
import com.ag_apps.core.presentation.designsystem.components.ShopyPasswordTextField
import com.ag_apps.core.presentation.designsystem.components.ShopyTextField
import com.ag_apps.core.presentation.ui.ObserveAsEvent
import org.koin.androidx.compose.koinViewModel

/**
 * @author Ahmed Guedmioui
 */
@Composable

fun LoginScreenCore(
    viewModel: LoginViewModel = koinViewModel(),
    onLoginSuccess: () -> Unit,
    inSignUpClick: () -> Unit,
) {

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    ObserveAsEvent(viewModel.event) { event ->
        when (event) {
            is LoginEvent.Error -> {
                keyboardController?.hide()
                Toast.makeText(
                    context, event.error.asString(context), Toast.LENGTH_SHORT
                ).show()
            }

            LoginEvent.LoginSuccess -> {
                keyboardController?.hide()

                Toast.makeText(
                    context,
                    context.getString(R.string.you_re_logged_in),
                    Toast.LENGTH_SHORT
                ).show()

                onLoginSuccess()
            }
        }
    }

    LoginScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                LoginAction.ObRegisterClick -> inSignUpClick()
                else -> Unit
            }

            viewModel.onAction(action)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit
) {

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            ShopyLargeTopBar(
                title = stringResource(R.string.login),
                windowInsets = WindowInsets(top = 0.dp)
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            ShopyTextField(
                textFieldState = state.email,
                startIcon = Icons.Outlined.Email,
                keyBoardType = KeyboardType.Email,
                hint = stringResource(R.string.example_email),
                title = stringResource(R.string.email),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            ShopyPasswordTextField(
                textFieldState = state.password,
                isPasswordVisible = state.isPasswordVisible,
                onTogglePasswordVisibility = {
                    onAction(LoginAction.OnTogglePasswordVisibilityClick)
                },
                hint = stringResource(R.string.password),
                title = stringResource(R.string.password),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            ShopyButton(
                text = stringResource(R.string.login),
                isLoading = state.isLoggingIn,
                enabled = state.canLogin,
                modifier = Modifier.fillMaxSize(),
                onClick = {
                    onAction(LoginAction.OnLoginClick)
                }
            )

            Spacer(modifier = Modifier.height(42.dp))

            Text(
                text = stringResource(R.string.or_login_with_google),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                isLoading = state.isLoggingIn,
                enabled = !state.isLoggingIn,
                modifier = Modifier.fillMaxSize(),
                onClick = {
                    onAction(LoginAction.OnGoogleLoginClick)
                }
            ) {
                Image(
                    painter = painterResource(R.drawable.google),
                    contentDescription = stringResource(R.string.google_login),
                    modifier = Modifier.size(27.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { onAction(LoginAction.ObRegisterClick) }
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.don_t_have_na_account) + " ",
                    fontFamily = Poppins,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowRightAlt,
                    contentDescription = stringResource(R.string.register),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(30.dp)
                )
            }

        }
    }

}

@Preview
@Composable
private fun LoginScreenPreview() {
    ShopyTheme {
        LoginScreen(
            state = LoginState(),
            onAction = {}
        )
    }
}










