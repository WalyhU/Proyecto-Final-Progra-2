package gt.edu.miumg.petstore.use_cases.FavoriteUseCases

import gt.edu.miumg.petstore.models.PetState
import gt.edu.miumg.petstore.repository.FavoriteRepository
import gt.edu.miumg.petstore.sign_in.UserData
import javax.inject.Inject

class DeleteFavorites @Inject constructor(
    private val repository: FavoriteRepository
) {
    operator fun invoke(userData: UserData, favorite: PetState) =
        repository.deleteFavorites(userData, favorite)
}