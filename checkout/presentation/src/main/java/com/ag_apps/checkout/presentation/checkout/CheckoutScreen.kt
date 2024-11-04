package com.ag_apps.checkout.presentation.checkout

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ag_apps.checkout.payment.PaymentSheet
import com.ag_apps.checkout.presentation.R
import com.ag_apps.core.domain.models.Address
import com.ag_apps.core.domain.models.Card
import com.ag_apps.core.domain.models.User
import com.ag_apps.core.presentation.DisclaimerInfoDialog
import com.ag_apps.core.presentation.EditeAddressDialog
import com.ag_apps.core.presentation.designsystem.ShopyTheme
import com.ag_apps.core.presentation.designsystem.components.ShopyButton
import com.ag_apps.core.presentation.designsystem.components.ShopyScaffold
import com.ag_apps.core.presentation.designsystem.components.ShopyTopBar
import com.ag_apps.core.presentation.ui.ObserveAsEvent
import org.koin.androidx.compose.koinViewModel

/**
 * @author Ahmed Guedmioui
 */
@Composable
fun CheckoutScreenCore(
    viewModel: CheckoutViewModel = koinViewModel(),
    onOrdersSubmitted: () -> Unit,
    onBack: () -> Unit,
) {

    val context = LocalContext.current
    ObserveAsEvent(viewModel.event) { event ->

        fun showToast(text: String) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }

        when (event) {
            is CheckoutEvent.AddressSaved -> {
                showToast(
                    if (event.isSaved) context.getString(R.string.address_saved)
                    else context.getString(R.string.address_not_saved)
                )
            }

            is CheckoutEvent.CardSaved -> {
                showToast(
                    if (event.isSaved) context.getString(R.string.card_saved)
                    else context.getString(R.string.card_not_saved)
                )
            }

            is CheckoutEvent.OrderSubmitted -> {
                if (event.isSubmitted) {
                    onOrdersSubmitted()
                } else {
                    showToast(context.getString(R.string.order_not_submitted))
                }
            }
        }
    }

    CheckoutScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                CheckoutAction.OnBackClick -> {
                    onBack()
                }

                else -> viewModel.onAction(action)
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CheckoutScreen(
    state: CheckoutState,
    onAction: (CheckoutAction) -> Unit,
) {
    var isCardAndAddressDisclaimerShowing by remember { mutableStateOf(false) }
    ShopyScaffold(
        topBar = {
            ShopyTopBar(
                titleText = stringResource(R.string.checkout),
                navigationIcon = Icons.Outlined.ArrowBackIosNew,
                onNavigationClick = { onAction(CheckoutAction.OnBackClick) },
                actionIcon = Icons.Outlined.Info,
                onActionClick = {
                    onAction(CheckoutAction.OnDisclaimerClick)
                    isCardAndAddressDisclaimerShowing = true
                },
            )
        }
    ) { paddingValues ->

        if (isCardAndAddressDisclaimerShowing) {
            DisclaimerInfoDialog(
                isBoth = true
            ) {
                isCardAndAddressDisclaimerShowing = false
            }
        }

        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {

            Box(modifier = Modifier.align(Alignment.Center)) {
                if (state.isLoading && !state.isError) {
                    CircularProgressIndicator()
                }
                if (state.isError) {
                    Text(
                        text = stringResource(R.string.can_t_load_checkout_details_right_now),
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            if (state.user != null && state.totalPrice != null) {
                Column(
                    modifier = Modifier
                ) {

                    Spacer(Modifier.height(34.dp))

                    if (state.user.address == null) {
                        CheckoutInfoSection(
                            action = stringResource(R.string.add),
                            title = stringResource(R.string.shipping_address) + " 📦",
                            headline = stringResource(R.string.no_address),
                            details = "",
                            onChangeClick = { onAction(CheckoutAction.OnAddressToggle) }
                        )
                    } else {
                        CheckoutInfoSection(
                            action = stringResource(R.string.edit),
                            title = stringResource(R.string.shipping_address) + " 📦",
                            headline = state.user.address?.street ?: "",
                            details = "${state.user.address?.city}, ${state.user.address?.region}, ${state.user.address?.zipCode}\n${state.user.address?.country}",
                            onChangeClick = { onAction(CheckoutAction.OnAddressToggle) }
                        )
                    }

                    // Not needed because stripe handles card data
//                    Spacer(Modifier.height(42.dp))
//
//                    if (state.card == null) {
//                        CheckoutInfoSection(
//                            action = stringResource(R.string.add),
//                            title = stringResource(R.string.payment_card) + " 💳",
//                            headline = stringResource(R.string.no_payment_card),
//                            details = "",
//                            onChangeClick = { onAction(CheckoutAction.OnCardToggle) }
//                        )
//                    } else {
//                        CheckoutInfoSection(
//                            action = stringResource(R.string.change),
//                            title = stringResource(R.string.payment_card) + " 💳",
//                            headline = state.card.nameOnCard,
//                            details = if (state.card.cardNumber.isNotEmpty()) "**** **** **** ${state.card.cardNumber.takeLast(4)}" else "",
//                            onChangeClick = { onAction(CheckoutAction.OnCardToggle) }
//                        )
//                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                        .align(Alignment.BottomCenter),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.total_price),
                            fontSize = 16.sp
                        )

                        Text(
                            text = "$${state.totalPrice}",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 19.sp
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    ShopyButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        text = stringResource(R.string.checkout_order),
                        enabled = state.user.address != null && !state.isLoadingPaymentSheet && !state.isProcessingOrder,
                        isLoading = state.isPaymentSheetShowing || state.isProcessingOrder,
                        onClick = {
                            onAction(CheckoutAction.OnCheckoutClick)
                        }
                    )

                    Spacer(Modifier.height(50.dp))

                }
            }
        }

        if (state.isPaymentSheetShowing && state.paymentConfig != null) {
            PaymentSheet(
                paymentConfig = state.paymentConfig,
                onPaymentResult = { result ->
                   onAction(CheckoutAction.OnSubmitResult(result))
                }
            )
        }

        var isAddressDisclaimerShowing by remember { mutableStateOf(false) }
        var isCardDisclaimerShowing by remember { mutableStateOf(false) }

        if (state.isEditeAddressShowing && !isAddressDisclaimerShowing) {
            EditeAddressDialog(
                streetTextState = state.streetTextState,
                cityTextState = state.cityTextState,
                regionTextState = state.regionTextState,
                zipcodeTextState = state.zipcodeTextState,
                countryTextState = state.countryTextState,
                canSavingAddress = state.canSavingAddress,
                isSavingAddress = state.isSavingAddress,
                onDisclaimerClick = { isAddressDisclaimerShowing = true },
                onSaveAddress = { onAction(CheckoutAction.OnSaveAddress) },
                onAddressToggle = { onAction(CheckoutAction.OnAddressToggle) },
            )
        }

//        if (state.isEditeCardShowing && !isCardDisclaimerShowing) {
//            EditeCardDialog(
//                nameOnCardTextState = state.nameOnCardTextState,
//                cardNumberTextState = state.cardNumberTextState,
//                expireDateTextState = state.expireDateTextState,
//                cvvTextState = state.cvvTextState,
//                canSavingCard = state.canSavingCard,
//                onDisclaimer = { isCardDisclaimerShowing = true },
//                onSaveCard = { onAction(CheckoutAction.OnSaveCard) },
//                onCardToggle = { onAction(CheckoutAction.OnCardToggle) },
//            )
//        }

        if (isAddressDisclaimerShowing || isCardDisclaimerShowing) {
            DisclaimerInfoDialog(
                isAddress = isAddressDisclaimerShowing,
                isCard = isCardDisclaimerShowing
            ) {
                isAddressDisclaimerShowing = false
                isCardDisclaimerShowing = false
            }
        }
    }
}

@Composable
fun CheckoutInfoSection(
    modifier: Modifier = Modifier,
    action: String,
    title: String,
    headline: String,
    details: String,
    onChangeClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(Modifier.height(16.dp))


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                .padding(20.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = headline,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Spacer(Modifier.width(16.dp))

                Text(
                    text = action,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onChangeClick() }
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = details,
            )
        }

    }
}


@Preview
@Composable
private fun ProfileScreenPreview() {
    ShopyTheme {
        CheckoutScreen(
            state = CheckoutState(
                user = User(
                    name = "Ahmed Guedmioui",
                    image = "",
                    email = "ahmed@gmail.com",
                    userId = "",
                    customerId = "",
                    address = Address(
                        street = "Hay Salam",
                        city = "Agadir",
                        region = "Souss-Massa",
                        zipCode = "87000",
                        country = "Morocco"
                    ),
                    cart = mapOf(),
                    wishlist = emptyList(),
                    orders = emptyList()
                ),
                card = Card(
                    nameOnCard = "Ahmed Guedmioui",
                    cardNumber = "1234 5678 9012 3456",
                    expireDate = "12/25",
                    cvv = "123",
                ),
                totalPrice = 23.45
            ),
            onAction = {},
        )
    }
}