package pl.sofantastica.data.model

data class FabricDto(
    val id: Int,
    val name: String,
    val supplier: String,
    val fabricUrl: String,
    val fabricUrlBig: String,
    val price: Double
)
