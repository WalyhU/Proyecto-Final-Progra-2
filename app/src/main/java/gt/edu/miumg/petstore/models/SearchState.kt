package gt.edu.miumg.petstore.models

data class SearchState(
    val search: String,
    val pets: MutableList<PetState>,
    val food: MutableList<FoodState>,
    val accessories: MutableList<AccessoryState>,
) {
    // Constructor secundario sin argumentos
    constructor() : this("", mutableListOf(), mutableListOf(), mutableListOf())
}