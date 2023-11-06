package gt.edu.miumg.petstore.models

data class CarritoState(
    val uid: String?,
    val image: String,
    val title: String,
    val description: String,
    val price: Double,
    var quantity: Int,
){
    // Constructor secundario sin argumentos
    constructor() : this(null, "", "", "", 0.0, 0)
}

