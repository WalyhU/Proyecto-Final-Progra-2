package gt.edu.miumg.petstore.models

data class SearchState(
    val search: String,
    val pets: MutableList<PetState>,
    val food: MutableList<PetState>,
    val accessories: MutableList<PetState>,
) {
    // Constructor secundario sin argumentos
    constructor() : this("", mutableListOf(), mutableListOf(), mutableListOf())
}