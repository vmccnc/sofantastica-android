package pl.sofantastica.domain.usecase.fabrics

import pl.sofantastica.data.model.FabricDto
import pl.sofantastica.data.repository.FabricRepository
import javax.inject.Inject

class GetFabricsUseCase @Inject constructor(
    private val repository: FabricRepository
) {
    suspend operator fun invoke() = emptyList<FabricDto>()//repository.ge()
}
