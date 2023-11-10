package gt.edu.miumg.petstore.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gt.edu.miumg.petstore.models.FavoriteState
import gt.edu.miumg.petstore.models.PetState
import gt.edu.miumg.petstore.sign_in.UserData
import gt.edu.miumg.petstore.use_cases.FavoriteUseCases.FavoriteUseCases
import gt.edu.miumg.petstore.util.Response
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteUseCases: FavoriteUseCases
) : ViewModel() {
    private val _getFavorites = mutableStateOf<Response<FavoriteState>>(Response.Success(null))
    val getFavoritesData: State<Response<FavoriteState>> = _getFavorites

    private val _setFavorite = mutableStateOf<Response<Boolean>>(Response.Success(null))
    val setFavoriteData: State<Response<Boolean>> = _setFavorite

    private val _deleteFavorite = mutableStateOf<Response<Boolean>>(Response.Success(null))
    val deleteFavoriteData: State<Response<Boolean>> = _deleteFavorite

    fun getFavoritesInfo(userData: UserData) {
        viewModelScope.launch {
            favoriteUseCases.getFavorites(userData).collect {
                _getFavorites.value = it
            }
        }
    }

    fun setFavoriteInfo(userData: UserData, favoriteState: FavoriteState) {
        viewModelScope.launch {
            favoriteUseCases.setFavorite(userData, favoriteState).collect {
                _setFavorite.value = it
            }
        }
    }

    fun deleteFavoriteInfo(userData: UserData, favoriteState: PetState) {
        viewModelScope.launch {
            favoriteUseCases.deleteFavorites(userData, favoriteState).collect {
                _setFavorite.value = it
            }
        }
    }
}