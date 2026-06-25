package com.furniture.duet.data.model.order

import com.furniture.duet.R

enum class PaymentType(val textId: Int, val logoId: Int) {
    CASH(R.string.cash_on_delivery, R.drawable.i_cash),
    CREDIT_CARD(R.string.payment_card, R.drawable.i_payment_card),
    ONLINE(R.string.fast_transfer, R.drawable.i_przelewy),
    BANK_TRANSFER(R.string.blik, R.drawable.i_blink)
}