package pl.sofantastica.domain.usecase.furnitures

import pl.sofantastica.data.repository.FurnitureRepository
import javax.inject.Inject

class GetFurnitureCatalogUseCase @Inject constructor(
    private val repository: FurnitureRepository
) {
    suspend operator fun invoke(minPrice: Double, maxPrice: Double, category: String?) =
        repository.getFurniture(minPrice, maxPrice, category)
}
