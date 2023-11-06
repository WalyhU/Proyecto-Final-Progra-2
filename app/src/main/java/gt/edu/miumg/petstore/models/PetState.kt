package gt.edu.miumg.petstore.models

data class PetState(
    val uid: String?,
    val image: String,
    val title: String,
    val description: String,
    val price: Double,
){
    // Constructor secundario sin argumentos
    constructor() : this(null, "", "", "", 0.0)
}
