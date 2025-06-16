package pl.sofantastica.domain.usecase

import pl.sofantastica.data.repository.FurnitureRepository
import javax.inject.Inject

class GetFurnitureCatalogUseCase @Inject constructor(
    private val repository: FurnitureRepository
) {
    suspend operator fun invoke() = repository.getFurniture()
}
