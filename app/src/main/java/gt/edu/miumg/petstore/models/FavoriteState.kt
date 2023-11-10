package gt.edu.miumg.petstore.models

data class FavoriteState(
    val items: MutableMap<String?, PetState>
) {
    // Constructor secundario sin argumentos
    constructor() : this(
        mutableMapOf(
            "item" to PetState(
                "",
                "",
                "",
                0.0
            )
        )
    )
}