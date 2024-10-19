package com.ag_apps.profile.presentation

import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ag_apps.core.domain.User
import com.ag_apps.core.presentation.DisclaimerInfoDialog
import com.ag_apps.core.presentation.designsystem.ShopyTheme
import com.ag_apps.core.presentation.designsystem.components.ShopyLargeTopBar
import com.ag_apps.core.presentation.ui.ObserveAsEvent
import com.ag_apps.profile.presentation.components.EditeAddressDialog
import com.ag_apps.profile.presentation.components.EditeCardDialog
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

        fun showToast(text: String) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }

        when (event) {
            is ProfileEvent.AddressSave -> {
                showToast(
                    if (event.isSaved) context.getString(R.string.address_saved)
                    else context.getString(R.string.address_not_saved)
                )
            }

            is ProfileEvent.CardSave -> {
                showToast(
                    if (event.isSaved) context.getString(R.string.card_saved)
                    else context.getString(R.string.card_not_saved)
                )
            }

            ProfileEvent.Logout -> {
                showToast(context.getString(R.string.you_are_logged_out))
                onLogoutClick()
            }
        }
    }

    ProfileScreen(
        state = viewModel.state,
        onAction = viewModel::onAction,
        onOrdersClick = onOrdersClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileScreen(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit,
    onOrdersClick: () -> Unit
) {

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            ShopyLargeTopBar(
                title = stringResource(R.string.my_profile),
                windowInsets = WindowInsets(top = 0.dp)
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(vertical = 16.dp)
                .fillMaxSize()
        ) {

            ProfileInfo(state = state)

            Spacer(Modifier.height(32.dp))

            ProfileActionSection(
                text = stringResource(R.string.my_orders),
                onClick = { onOrdersClick() }
            )

            ProfileActionSection(
                text = stringResource(R.string.my_address),
                onClick = { onAction(ProfileAction.OnAddressToggle) }
            )

            ProfileActionSection(
                text = stringResource(R.string.log_out),
                color = Color.Red,
                onClick = { onAction(ProfileAction.OnLogoutClick) }
            )
        }

        var isAddressDisclaimerShowing by remember { mutableStateOf(false) }
        var isCardDisclaimerShowing by remember { mutableStateOf(false) }

        if (state.isEditeAddressShowing && !isAddressDisclaimerShowing) {
            EditeAddressDialog(
                onAction = onAction,
                state = state,
                onDisclaimerClick = { isAddressDisclaimerShowing = true },
            )
        }

        if (state.isEditeCardShowing && !isCardDisclaimerShowing) {
            EditeCardDialog(
                onAction = onAction,
                state = state,
                onDisclaimer = { isCardDisclaimerShowing = true }
            )
        }

        if (isAddressDisclaimerShowing || isCardDisclaimerShowing) {
            DisclaimerInfoDialog(isAddress = isAddressDisclaimerShowing) {
                isAddressDisclaimerShowing = false
                isCardDisclaimerShowing = false
            }
        }
    }
}

@Composable
fun ProfileInfo(
    modifier: Modifier = Modifier,
    state: ProfileState
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = state.user?.image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
        )

        Spacer(Modifier.width(16.dp))

        Column {
            Text(
                text = state.user?.name ?: "",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = state.user?.email ?: "",
                fontSize = 16.sp
            )

        }
    }
}

@Composable
fun ProfileActionSection(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = MaterialTheme.colorScheme.onBackground,
    onClick: () -> Unit
) {
    HorizontalDivider(modifier = Modifier.alpha(0.3f))

    Row(
        modifier = modifier
            .clickable { onClick() }
            .padding(vertical = 22.dp, horizontal = 22.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            color = color,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = text,
            tint = color
        )
    }
}


@Preview
@Composable
private fun ProfileScreenPreview() {
    ShopyTheme {
        ProfileScreen(
            state = ProfileState(
                user = User(
                    name = "Ahmed Guedmioui",
                    image = "",
                    email = "ahmed@gmail.com",
                    userId = "",
                    address = null,
                    cart = emptyList(),
                    wishlist = emptyList()
                ),
                isEditeAddressShowing = true
            ),
            onAction = {},
            onOrdersClick = {}
        )
    }
}