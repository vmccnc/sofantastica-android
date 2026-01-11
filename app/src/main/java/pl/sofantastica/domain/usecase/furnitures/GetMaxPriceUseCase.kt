package pl.sofantastica.domain.usecase.furnitures

import pl.sofantastica.data.repository.FurnitureRepository
import javax.inject.Inject

class GetMaxPriceUseCase @Inject constructor(
    private val repository: FurnitureRepository
) {
    suspend operator fun invoke() = repository.getMaxPrice()
}
