package gt.edu.miumg.petstore.models
data class CartItem(
    val description: Any?,
    val image: String?,
    val price: Double?,
    val quantity: Long?,
    val title: String?
) {
}

data class CartState(
    val items: MutableMap<String?, CartItem>
) {
    // Constructor secundario sin argumentos
    constructor() : this(
        mutableMapOf(
            "item" to CartItem(
                "",
                "",
                0.0,
                0,
                ""
            )
        )
    )
}