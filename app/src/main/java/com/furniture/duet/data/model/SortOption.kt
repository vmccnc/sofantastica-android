package com.furniture.duet.data.model

import com.furniture.duet.R

enum class SortOption(
    val textId: Int,
    val sortBy: String,
    val sortOrder: String
) {
    SORT_BY_PRICE_ASC(
        textId = R.string.sort_price_asc,
        sortBy = "price",
        sortOrder = "asc"
    ),
    SORT_BY_PRICE_DESC(
        textId = R.string.sort_price_desc,
        sortBy = "price",
        sortOrder = "desc"
    ),
    SORT_NEW(
        textId = R.string.sort_new,
        sortBy = "createdAt",
        sortOrder = "desc"
    ),
    SORT_POPULAR(
        textId = R.string.sort_popular,
        sortBy = "",
        sortOrder = ""
    )
}