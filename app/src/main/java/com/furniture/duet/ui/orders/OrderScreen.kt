package com.furniture.duet.ui.orders

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.furniture.duet.R
import com.furniture.duet.data.model.order.DeliveryMethodType
import com.furniture.duet.data.model.order.PaymentType
import com.furniture.duet.ui.cart.CartViewModel
import com.furniture.duet.ui.main.MainViewModel
import com.furniture.duet.ui.theme.EnabledBtnColor
import com.furniture.duet.ui.theme.FabricSecondaryColor
import com.furniture.duet.ui.theme.FurnitureDetailTextColor
import com.furniture.duet.ui.theme.SearchBarBackgroundColor
import com.furniture.duet.ui.theme.TitleColor
import kotlin.reflect.jvm.internal.impl.types.checker.TypeRefinementSupport.Enabled

@Composable
fun MakeOrderScreen(
    cartTotal: Int,
    goToOrderHistory: () -> Unit,
    viewModel: OrderViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val margin_16 = dimensionResource(R.dimen.margin_16)
    val margin_2 = dimensionResource(R.dimen.margin_2)
    val margin_20 = dimensionResource(R.dimen.margin_20)

    if (viewModel.isOrderSent) {
        Dialog(onDismissRequest = {}) {
            Column(
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(margin_20))
                    .padding(margin_20)
            ) {
                Text(
                    text = stringResource(R.string.thank_you_for_sending_the_order),
                    style = MaterialTheme.typography.labelMedium,
                    color = FurnitureDetailTextColor
                )
                Text(
                    text = stringResource(R.string.we_will_inform_you_when_your_order_has_been_accepted_for_implementation),
                    style = MaterialTheme.typography.bodyMedium,
                    color = FabricSecondaryColor,
                    modifier = Modifier.padding(vertical = margin_20)
                )
                Button(
                    modifier = Modifier
                        .padding(vertical = margin_16)
                        .fillMaxWidth(),
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EnabledBtnColor,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        modifier = Modifier.clickable { goToOrderHistory() },
                        text = stringResource(R.string.view_the_order),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
    Column {
        Text(
            text = stringResource(R.string.making_an_order_title),
            style = MaterialTheme.typography.labelSmall,
            color = FurnitureDetailTextColor
        )
        Text(
            text = stringResource(R.string.address_title),
            style = MaterialTheme.typography.bodyLarge,
            color = FurnitureDetailTextColor
        )
        StyledTextField(viewModel.fullName, viewModel::setNewFullName,
            stringResource(R.string.full_name_placeholder))
        StyledTextField(viewModel.country, viewModel::setNewCountry,
            stringResource(R.string.country_placeholder), viewModel.isAddressEnabled())
        StyledTextField(viewModel.city, viewModel::setNewCity,
            stringResource(R.string.city_placeholder), viewModel.isAddressEnabled())
        StyledTextField(viewModel.address, viewModel::setNewAddress,
            stringResource(R.string.address_placeholder), viewModel.isAddressEnabled())
        StyledTextField(viewModel.postalCode, viewModel::setNewPostalCode,
            stringResource(R.string.postal_code_placeholder), viewModel.isAddressEnabled())
        StyledTextField(viewModel.email, viewModel::setNewEmail,
            stringResource(R.string.email_placeholder))
        StyledTextField(viewModel.phone, viewModel::setNewPhone,
            stringResource(R.string.phone_placeholder))

        val tipColor = Color.Black.copy(.45f)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                modifier = Modifier
                    .padding(horizontal = margin_2, vertical = margin_16)
                    .size(margin_16),
                checked = viewModel.isBusiness,
                onCheckedChange = { viewModel.isBusiness = it },
                colors = CheckboxDefaults.colors(
                    checkmarkColor = tipColor,
                    uncheckedColor = tipColor,
                    checkedColor = tipColor,
                )
            )
            Text(
                text = "\tIs Business",
                color = tipColor,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (viewModel.isBusiness) {
            StyledTextField(viewModel.unn, viewModel::setNewUNN,
                stringResource(R.string.unn_placeholder))
            StyledTextField(viewModel.companyName, viewModel::setNewCompanyName,
                stringResource(R.string.company_placeholder))
        }

        DeliveryOptionSelector()

        PaymentOptionSelector()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.cart_total),
                style = MaterialTheme.typography.bodyMedium,
                color = FurnitureDetailTextColor
            )

            val total = cartTotal + (viewModel.selectedDeliveryOption?.minPrice ?: 0)

            Text(
                text = stringResource(R.string.furniture_total_price).format(total),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = FurnitureDetailTextColor
            )
        }

        Text(
            text = stringResource(R.string.including_shipping),
            style = MaterialTheme.typography.bodyMedium,
            color = TitleColor
        )

        Button(
            modifier = Modifier
                .padding(vertical = margin_16)
                .fillMaxWidth(),
            onClick = {
                viewModel.createOrder()
                mainViewModel.clearCartCount()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = EnabledBtnColor,
                contentColor = Color.White
            )
        ) {
            Text(
                text = stringResource(R.string.go_father),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
fun StyledTextField(
    value: String,
    changeValue: (String) -> Unit,
    placeholder: String,
    enabled: Boolean = true
) {
    val margin_60 = dimensionResource(R.dimen.margin_60)
    val margin_5 = dimensionResource(R.dimen.margin_5)
    TextField(
        value = value,
        onValueChange = changeValue,
        textStyle = MaterialTheme.typography.bodyMedium,
        enabled = enabled,
        placeholder = {
            Text(text = placeholder, style = MaterialTheme.typography.bodyMedium)
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = SearchBarBackgroundColor,
            unfocusedContainerColor = SearchBarBackgroundColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledContainerColor = Color.Gray
        ),
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = margin_5)
            .clip(shape = RoundedCornerShape(margin_60))
    )
}

@Composable
fun DeliveryOptionSelector(viewModel: OrderViewModel = hiltViewModel()){
    val margin_10 = dimensionResource(R.dimen.margin_10)
    val size_24 = dimensionResource(R.dimen.size_24)

    Text(
        text = stringResource(R.string.delivery_title),
        style = MaterialTheme.typography.bodyLarge,
        color = EnabledBtnColor
    )

    DeliveryMethodType.values().forEach { deliveryOption ->
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = margin_10)
                .background(Color.White, RoundedCornerShape(size_24))
                .padding(margin_10)
                .selectable(
                    selected = (deliveryOption == viewModel.selectedDeliveryOption),
                    onClick = { viewModel.selectDeliveryOption(deliveryOption) },
                    role = Role.RadioButton
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = (deliveryOption == viewModel.selectedDeliveryOption),
                onClick = { viewModel.selectDeliveryOption(deliveryOption) }
            )
            Column {
                Text(
                    text = stringResource(deliveryOption.textId),
                    style = MaterialTheme.typography.labelSmall,
                    color = EnabledBtnColor
                )
                Text(
                    text = "Nearest delivery date",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TitleColor
                )
                Text(
                    text = stringResource(R.string.delivery_price).format(deliveryOption.minPrice),
                    style = MaterialTheme.typography.bodyMedium,
                    color = EnabledBtnColor,
                    modifier = Modifier.padding(top = margin_10)
                )
            }
        }
    }
}

@Composable
fun PaymentOptionSelector(viewModel: OrderViewModel = hiltViewModel()){
    val margin_10 = dimensionResource(R.dimen.margin_10)
    val size_24 = dimensionResource(R.dimen.size_24)

    Text(
        text = stringResource(R.string.payment_title),
        style = MaterialTheme.typography.bodyLarge,
        color = EnabledBtnColor
    )
    PaymentType.entries.forEach { paymentOption ->
        val backgroundColor =
            if (paymentOption == PaymentType.CASH) Color.White
            else Color.Gray

        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = margin_10)
                .background(backgroundColor, RoundedCornerShape(size_24))
                .padding(margin_10)
                .selectable(
                    selected = (paymentOption == viewModel.selectedPaymentOption),
                    onClick = { viewModel.selectPaymentOption(paymentOption) },
                    role = Role.RadioButton
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = (paymentOption == viewModel.selectedPaymentOption),
                onClick = { viewModel.selectPaymentOption(paymentOption) },
                enabled = paymentOption == PaymentType.CASH,
//                selected = (paymentOption == viewModel.selectedPaymentOption),
//                onClick = { viewModel.selectPaymentOption(paymentOption) }
            )
            Text(
                text = stringResource(paymentOption.textId),
                style = MaterialTheme.typography.labelSmall,
                color = EnabledBtnColor
            )
            Spacer(Modifier.weight(1f))
            Image(
                painter = painterResource(paymentOption.logoId),
                contentDescription = null,
                modifier = Modifier.padding(margin_10)
            )
        }
    }
}