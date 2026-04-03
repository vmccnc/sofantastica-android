package com.furniture.duet.data.repository

import com.furniture.duet.data.model.fabric.FabricDto

interface FabricRepository {
    suspend fun getAllFabrics(): HashMap<String, List<FabricDto>>
}
