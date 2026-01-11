package pl.sofantastica.domain.usecase.fabrics

import pl.sofantastica.data.repository.FabricRepository
import javax.inject.Inject

class GetPopularFabricsUseCase @Inject constructor(
    private val repository: FabricRepository
) {
    suspend operator fun invoke() = repository.getPopularFabrics()
}
