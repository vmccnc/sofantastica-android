package com.furniture.duet.data.model

import com.furniture.duet.R

enum class SortOption(val textId: Int) {
    SORT_BY_PRICE_ASC(R.string.sort_price_asc),
    SORT_BY_PRICE_DESC(R.string.sort_price_desc),
    SORT_NEW(R.string.sort_new),
    SORT_POPULAR(R.string.sort_popular)
}