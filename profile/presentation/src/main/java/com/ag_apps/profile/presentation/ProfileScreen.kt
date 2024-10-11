package com.ag_apps.profile.presentation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.ag_apps.core.domain.User
import com.ag_apps.core.presentation.designsystem.ShopyTheme
import com.ag_apps.core.presentation.ui.ObserveAsEvent
import org.koin.androidx.compose.koinViewModel

/**
 * @author Ahmed Guedmioui
 */
@Composable
fun ProfileScreenCore(
    viewModel: ProfileViewModel = koinViewModel(),
    onOrdersClick: () -> Unit,
    onLogoutClick: () -> Unit
) {

    val context = LocalContext.current
    ObserveAsEvent(viewModel.event) { event ->
        when (event) {
            is ProfileEvent.OnAddressSave -> {
                Toast.makeText(
                    context,
                    if (event.isSaved) context.getString(R.string.address_saved)
                    else context.getString(R.string.address_not_saved),
                    Toast.LENGTH_SHORT
                ).show()
            }

            is ProfileEvent.OnCardSave -> {
                Toast.makeText(
                    context,
                    if (event.isSaved) context.getString(R.string.card_saved)
                    else context.getString(R.string.card_not_saved),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    ProfileScreen(
        state = viewModel.state,
        onAction = viewModel::onAction,
        onOrdersClick = onOrdersClick,
        onLogoutClick = onLogoutClick
    )
}

@Composable
private fun ProfileScreen(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit,
    onOrdersClick: () -> Unit,
    onLogoutClick: () -> Unit
) {

}

@Preview
@Composable
private fun ProfileScreenPreview() {
    ShopyTheme {
        ProfileScreen(
            state = ProfileState(
                user = User(
                    name = "Ahmed Guedmioui",
                    email = "ahmed@gmail.com",
                    id = "",
                    card = null,
                    address = null
                )
            ),
            onAction = {},
            onOrdersClick = {},
            onLogoutClick = {}
        )
    }
}