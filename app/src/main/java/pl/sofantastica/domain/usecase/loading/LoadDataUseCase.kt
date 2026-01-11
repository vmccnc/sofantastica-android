package pl.sofantastica.domain.usecase.loading

import pl.sofantastica.data.repository.FabricRepository
import pl.sofantastica.data.repository.FurnitureRepository
import javax.inject.Inject

class LoadDataUseCase @Inject constructor(
    private val furnitureRepo: FurnitureRepository,
    private val fabricRepo: FabricRepository
) {
    suspend operator fun invoke() = furnitureRepo.loadFurniture() && fabricRepo.loadFabrics()
}
