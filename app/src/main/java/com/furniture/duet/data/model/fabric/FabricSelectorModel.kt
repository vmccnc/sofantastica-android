package com.furniture.duet.data.model.fabric

data class FabricSelectorModel(
    val fabricSuppliers: List<String>,
    val fabricsBySuppliers: List<List<FabricDto>>
)