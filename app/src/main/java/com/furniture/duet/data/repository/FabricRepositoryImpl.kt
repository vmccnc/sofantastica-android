package com.furniture.duet.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.furniture.duet.background.InternetConnectionManager
import com.furniture.duet.data.api.RetrofitApiService
import com.furniture.duet.data.model.fabric.FabricDto
import javax.inject.Inject

class FabricRepositoryImpl @Inject constructor(
    private val api: RetrofitApiService,
    private val connectionManager: InternetConnectionManager
) : FabricRepository {

    companion object {
        private var listOfFabric: HashMap<String, List<FabricDto>> = LinkedHashMap()
    }

    override suspend fun getAllFabrics() = withContext(Dispatchers.IO) {
        connectionManager.isOnline()
        if (listOfFabric.isEmpty()) {
            listOfFabric["Popular"] = api.listPopularFabrics().body() ?: emptyList()
            api.listSuppliers().body()?.forEach { supplier ->
                listOfFabric[supplier] = api.listFabricsBySupplier(supplier).body() ?: emptyList()
            }
        }
        listOfFabric
    }
}
