package pl.sofantastica.domain.usecase

import pl.sofantastica.data.repository.FurnitureRepository
import javax.inject.Inject

class GetFurnitureDetailUseCase @Inject constructor(
    private val repository: FurnitureRepository
) {
    suspend operator fun invoke(id: Int) = repository.getFurnitureDetail(id)
}
