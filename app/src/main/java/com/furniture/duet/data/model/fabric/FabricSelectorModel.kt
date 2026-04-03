package com.furniture.duet.data.model.fabric

data class FabricSelectorModel(
    val suppliers: List<String>,
    val fabricsBySuppliers: List<List<FabricDto>>
)