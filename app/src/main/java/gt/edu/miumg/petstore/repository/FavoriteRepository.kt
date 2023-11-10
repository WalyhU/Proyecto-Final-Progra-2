package gt.edu.miumg.petstore.repository

import gt.edu.miumg.petstore.models.FavoriteState
import gt.edu.miumg.petstore.models.PetState
import gt.edu.miumg.petstore.sign_in.UserData
import gt.edu.miumg.petstore.util.Response
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getFavorites(userData: UserData): Flow<Response<FavoriteState>>
    fun setFavorite(userData: UserData, favorite: FavoriteState): Flow<Response<Boolean>>

    fun deleteFavorites(userData: UserData, favorite: PetState): Flow<Response<Boolean>>
}