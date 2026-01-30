package com.furniture.duet.domain.usecase

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import com.furniture.duet.data.model.fabric.FabricDto
import com.furniture.duet.data.repository.FabricRepository
import com.furniture.duet.domain.usecase.fabrics.GetFabricsUseCase

class GetFabricsUseCaseTest {
    private val repo = object : FabricRepository {
        override suspend fun loadFabrics(): Boolean {
            TODO("Not yet implemented")
        }

        override suspend fun getFabric(id: Int) = FabricDto(
            id, "Name$id", "B",
            fabricUrl = TODO(),
            fabricUrlBig = TODO(),
            price = TODO()
        )

        override suspend fun getSuppliers(): List<String> {
            TODO("Not yet implemented")
        }

        override suspend fun getFabricsBySupplier(supplier: String) = emptyList<FabricDto>()
    }

    private val useCase = GetFabricsUseCase(repo)

    @Test
    fun invoke_returnsList() = runBlocking {
        val result = useCase()
        assertEquals(1, result.size)
        assertEquals("Cotton", result.first().name)
    }
}
