package com.ag_apps.auth.presentation.register

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowRightAlt
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ag_apps.auth.domain.PasswordValidationState
import com.ag_apps.auth.domain.UserDataValidator
import com.ag_apps.auth.presentation.R
import com.ag_apps.core.presentation.designsystem.Poppins
import com.ag_apps.core.presentation.designsystem.ShopyTheme
import com.ag_apps.core.presentation.designsystem.components.ShopyButton
import com.ag_apps.core.presentation.designsystem.components.ShopyOutlinedButton
import com.ag_apps.core.presentation.designsystem.components.ShopyLargeTopBar
import com.ag_apps.core.presentation.designsystem.components.ShopyPasswordTextField
import com.ag_apps.core.presentation.designsystem.components.ShopyScaffold
import com.ag_apps.core.presentation.designsystem.components.ShopyTextField
import com.ag_apps.core.presentation.ui.ObserveAsEvent
import org.koin.androidx.compose.koinViewModel


/**
 * @author Ahmed Guedmioui
 */
@Composable
fun RegisterScreenCore(
    onLoginClick: () -> Unit,
    onSuccessfulRegistration: () -> Unit,
    viewModel: RegisterViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    ObserveAsEvent(flow = viewModel.event) { event ->
        when (event) {
            is RegisterEvent.Error -> {
                keyboardController?.hide()
                Toast.makeText(
                    context,
                    event.error.asString(context),
                    Toast.LENGTH_SHORT
                ).show()
            }

            RegisterEvent.RegistrationSuccess -> {
                keyboardController?.hide()

                Toast.makeText(
                    context,
                    context.getString(R.string.you_re_registered),
                    Toast.LENGTH_SHORT
                ).show()

                onSuccessfulRegistration()
            }

            else -> {}
        }
    }

    RegisterScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                RegisterAction.OnLoginClick -> onLoginClick()
                else -> Unit
            }

            viewModel.onAction(action)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegisterScreen(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit
) {
    ShopyScaffold (
        topBar = {
            ShopyLargeTopBar(
                titleText = stringResource(R.string.register),
                windowInsets = WindowInsets(top = 0.dp),
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.background
                )
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
                endIcon = if (state.isEmailValid) Icons.Outlined.Check else null,
                hint = stringResource(R.string.example_email),
                title = stringResource(R.string.email),
                modifier = Modifier.fillMaxWidth(),
                keyBoardType = KeyboardType.Email,
            )

            Spacer(modifier = Modifier.height(16.dp))

            ShopyTextField(
                textFieldState = state.name,
                startIcon = Icons.Outlined.PersonOutline,
                endIcon = if (state.isNameValid) Icons.Outlined.Check else null,
                hint = stringResource(R.string.example_name),
                title = stringResource(R.string.name),
                modifier = Modifier.fillMaxWidth(),
                keyBoardType = KeyboardType.Email,
            )

            Spacer(modifier = Modifier.height(16.dp))

            ShopyPasswordTextField(
                textFieldState = state.password,
                isPasswordVisible = state.isPasswordVisible,
                onTogglePasswordVisibility = {
                    onAction(RegisterAction.OnTogglePasswordVisibilityClick)
                },
                hint = stringResource(R.string.password),
                title = stringResource(R.string.password),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            PasswordRequirements(
                text = stringResource(
                    R.string.at_least_x_characters,
                    UserDataValidator.MIN_PASSWORD_LENGTH
                ),
                isValid = state.passwordValidationState.hasMinimumLength
            )
            Spacer(modifier = Modifier.height(4.dp))

            PasswordRequirements(
                text = stringResource(R.string.at_least_one_number),
                isValid = state.passwordValidationState.hasDigit
            )
            Spacer(modifier = Modifier.height(4.dp))

            PasswordRequirements(
                text = stringResource(R.string.contains_a_lowercase_letter),
                isValid = state.passwordValidationState.hasLowercaseLetter
            )
            Spacer(modifier = Modifier.height(4.dp))

            PasswordRequirements(
                text = stringResource(R.string.contains_a_uppercase_letter),
                isValid = state.passwordValidationState.hasUppercaseLetter
            )
            Spacer(modifier = Modifier.height(22.dp))

            ShopyButton(
                text = stringResource(R.string.register),
                isLoading = state.isRegistering,
                enabled = state.canRegister,
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onAction(RegisterAction.ObRegisterClick)
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(R.string.or_login_with_google),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            ShopyOutlinedButton(
                isLoading = state.isRegistering,
                enabled = !state.isRegistering,
                modifier = Modifier.fillMaxSize(),
                onClick = {
                    onAction(RegisterAction.OnGoogleRegisterClick)
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
                    .clickable { onAction(RegisterAction.OnLoginClick) }
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.already_have_an_account) + " ",
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

@Composable
fun PasswordRequirements(
    modifier: Modifier = Modifier,
    text: String,
    isValid: Boolean
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (isValid) {
                Icons.Outlined.Check
            } else {
                Icons.Outlined.Close
            },
            contentDescription = null,
            tint = if (isValid) {
                Color.Green
            } else {
                MaterialTheme.colorScheme.error
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp
        )
    }

}

@Preview
@Composable
private fun RegisterScreenCoreScreenPreview() {
    ShopyTheme {
        RegisterScreen(
            state = RegisterState(
                passwordValidationState = PasswordValidationState(
                    hasDigit = true,
                ),
            ),
            onAction = {}
        )
    }
}








