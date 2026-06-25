package com.furniture.duet.domain.usecase.cart

import android.content.Context
import com.furniture.duet.R
import com.furniture.duet.data.data_store.CountStorage
import com.furniture.duet.data.db.dao.CartDao
import com.furniture.duet.data.repository.CartRepository
import com.furniture.duet.domain.exceptions.ResIdException
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GetCartCountUseCase @Inject constructor(
    //@ApplicationContext private val context: Context,
    private val cartDao: CartDao
) {
    operator fun invoke() = cartDao.getCartCount()//CountStorage.getCartCount(context)
}