package com.ag_apps.core.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowRightAlt
import androidx.compose.material.icons.automirrored.rounded.FormatListBulleted
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ag_apps.core.presentation.designsystem.components.ShopyTextField
import com.ag_apps.core.presentation.ui.R

/**
 * @author Ahmed Guedmioui
 */

@Composable
fun ProductsFilter(
    modifier: Modifier = Modifier,
    isFilterOpen: Boolean,
    minPriceState: TextFieldState,
    maxPriceState: TextFieldState,
    toggleFilter: () -> Unit,
    toggleProductsLayout: () -> Unit,
    applyFilter: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = Icons.Rounded.FilterList,
            contentDescription = stringResource(R.string.products_filter),
            modifier = Modifier
                .size(30.dp)
                .clickable { toggleFilter() }
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (minPriceState.text.isNotEmpty()) {
                Text(
                    text = "$" + minPriceState.text.toString()
                )
            }
            if (minPriceState.text.isNotEmpty() || maxPriceState.text.isNotEmpty()) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowRightAlt,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(28.dp)
                )
            }
            if (maxPriceState.text.isNotEmpty()) {
                Text(
                    text = "$" + maxPriceState.text.toString()
                )
            }
        }

        Icon(
            imageVector = Icons.AutoMirrored.Rounded.FormatListBulleted,
            contentDescription = stringResource(R.string.change_products_layout),
            modifier = Modifier
                .size(28.dp)
                .clickable { toggleProductsLayout() }
        )
    }

    if (isFilterOpen) {

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ShopyTextField(
                textFieldState = minPriceState,
                hint = stringResource(R.string.min_price),
                applyTextWeight = false,
                textVerticalPadding = 10.dp,
                textSize = 15.sp,
                keyBoardType = KeyboardType.Number,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowRightAlt,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(28.dp)
            )

            ShopyTextField(
                textFieldState = maxPriceState,
                hint = stringResource(R.string.max_price),
                applyTextWeight = false,
                textVerticalPadding = 10.dp,
                textSize = 15.sp,
                keyBoardType = KeyboardType.Number,
                modifier = Modifier.weight(1f)
            )

            Spacer(Modifier.width(16.dp))

            Text(
                text = stringResource(R.string.apply),
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clickable { applyFilter() }
            )
        }
    }
}