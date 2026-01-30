package com.furniture.duet.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.furniture.duet.background.InternetConnectionManager
import com.furniture.duet.data.api.RetrofitApiService
import com.furniture.duet.data.db.dao.FabricDao
import com.furniture.duet.data.db.entity.FabricEntity
import com.furniture.duet.data.db.entity.SupplierEntity
import com.furniture.duet.data.model.fabric.FabricDto
import javax.inject.Inject

class FabricRepositoryImpl @Inject constructor(
    private val api: RetrofitApiService,
    private val dao: FabricDao,
    private val connectionManager: InternetConnectionManager
) : FabricRepository {

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override suspend fun loadFabrics() = withContext(Dispatchers.IO) {
        connectionManager.isOnline()
        val responseFabrics = api.listFabrics()

        if (responseFabrics.isSuccessful) {
            responseFabrics.body()?.let {
                val list = it.stream().map { fabric ->
                    FabricEntity(
                        id = fabric.id,
                        name = fabric.name,
                        supplier = fabric.supplier,
                        fabricUrl = fabric.fabricUrl,
                        fabricUrlBig = fabric.fabricUrlBig,
                        price = fabric.price
                    )
                }.toList()
                dao.deleteAllFabrics()
                dao.insertAll(list)
            }
        }

        val responseSuppliers = api.listSuppliers()

        if (responseSuppliers.isSuccessful) {
            responseSuppliers.body()?.let {
                dao.deleteAllSuppliers()
                dao.insertSuppliers(it.stream().map { SupplierEntity(it) }.toList())
            }
        }
        true
    }

    override suspend fun getFabric(id: Int): FabricDto? = withContext(Dispatchers.IO){
        connectionManager.isOnline()
        dao.getFabric(id)
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override suspend fun getSuppliers(): List<String> = withContext(Dispatchers.IO) {
        connectionManager.isOnline()
        dao.getSuppliers()
    }

//    override suspend fun getPopularFabrics(): List<FabricDto> = withContext(Dispatchers.IO) {
//        connectionManager.isOnline()
//        val response = api.listPopularFabrics()
//        if (response.isSuccessful) {
//            return@withContext response.body() ?: emptyList()
//        }
//        throw HttpException(response)
//    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override suspend fun getFabricsBySupplier(supplier: String): List<FabricDto> = withContext(Dispatchers.IO) {
        connectionManager.isOnline()
        dao.getFabricsBySupplier(supplier)
    }
}
