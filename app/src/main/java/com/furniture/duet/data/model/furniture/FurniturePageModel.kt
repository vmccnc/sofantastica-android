package com.furniture.duet.data.model.furniture

data class FurniturePageModel(
    val content: List<FurnitureDto>,
    val page : Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val isFirst: Boolean,
    val isLast: Boolean
)