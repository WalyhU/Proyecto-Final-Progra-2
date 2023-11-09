package gt.edu.miumg.petstore.models

data class FoodState(
    val image: String?,
    val title: String?,
    val description: String?,
    val price: Double?,
){
    // Constructor secundario sin argumentos
    constructor() : this("", "", "", 0.0)
}
