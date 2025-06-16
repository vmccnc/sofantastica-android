package pl.sofantastica.domain.usecase

import pl.sofantastica.data.repository.FabricRepository
import javax.inject.Inject

class GetFabricsUseCase @Inject constructor(
    private val repository: FabricRepository
) {
    suspend operator fun invoke() = repository.getFabrics()
}
