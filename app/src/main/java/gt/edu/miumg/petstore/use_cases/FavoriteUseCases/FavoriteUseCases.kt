package gt.edu.miumg.petstore.use_cases.FavoriteUseCases

data class FavoriteUseCases (
    val setFavorite: SetFavorite,
    val getFavorites: GetFavorites,
    val deleteFavorites: DeleteFavorites
)