package gt.edu.miumg.petstore.use_cases.CartUseCases

data class CartUseCases(
    val setCart: SetCart,
    val getCart: GetCart,
    val deleteElement: DeleteElement
)
