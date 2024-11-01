package com.ag_apps.core.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.ag_apps.core.presentation.designsystem.ShopyTheme
import com.ag_apps.core.presentation.designsystem.components.ShopyButton
import com.ag_apps.core.presentation.designsystem.components.ShopyDialog
import com.ag_apps.core.presentation.designsystem.components.ShopyOutlinedButton
import com.ag_apps.core.presentation.ui.R

/**
 * @author Ahmed Guedmioui
 */

@Composable
fun DisclaimerInfoDialog(
    modifier: Modifier = Modifier,
    isAddress: Boolean = false,
    isCard: Boolean = false,
    isBoth: Boolean = false,
    onDismiss: () -> Unit
) {
    var description = ""

    if (isAddress) {
        description = stringResource(R.string.disclaimer_address_info)
    }
    if (isCard) {
        description = stringResource(R.string.disclaimer_card_info)
    }
    if (isBoth) {
        description = stringResource(R.string.disclaimer_card_and_address_info)
    }

    ShopyDialog(
        modifier = modifier
            .padding(horizontal = 16.dp),
        title = stringResource(R.string.disclaimer),
        description = description,
        descriptionTextAlign = TextAlign.Start,
        onDismiss = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        primaryButton = {
            ShopyButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.okay),
                onClick = onDismiss
            )
        },
    )
}

@Preview
@Composable
private fun DisclaimerInfoDialogPreview() {
    ShopyTheme {
        DisclaimerInfoDialog(isBoth = true) {}
    }
}