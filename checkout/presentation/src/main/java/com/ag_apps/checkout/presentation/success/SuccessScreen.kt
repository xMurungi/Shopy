package com.ag_apps.checkout.presentation.success

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ag_apps.checkout.presentation.R
import com.ag_apps.core.presentation.designsystem.ShopyTheme
import com.ag_apps.core.presentation.designsystem.components.ShopyButton

/**
 * @author Ahmed Guedmioui
 */
@Composable
fun SuccessScreen(
    onContinue: () -> Unit
) {

    Image(
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop,
        painter = painterResource(id = R.drawable.success),
        contentDescription = null
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Spacer(Modifier.height(100.dp))

        Text(
            text = stringResource(R.string.success),
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            color = Color.Black
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.thank_you_for_choosing_our_app),
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )

        Spacer(Modifier.height(40.dp))

        ShopyButton(
            text = stringResource(R.string.continue_shopping),
            onClick = onContinue
        )

    }

}

@Preview
@Composable
private fun SuccessScreenPreview() {
    ShopyTheme {
        SuccessScreen { }
    }
}