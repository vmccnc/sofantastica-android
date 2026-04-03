package com.furniture.duet.data.model.furniture

data class CatalogModel(
    val list: List<FurnitureCatalogModel> = emptyList(),
    val page: Int = 0,
    val isLast: Boolean = false
)