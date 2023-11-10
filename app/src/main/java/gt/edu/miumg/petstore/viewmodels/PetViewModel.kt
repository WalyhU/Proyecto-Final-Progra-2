package gt.edu.miumg.petstore.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gt.edu.miumg.petstore.models.PetState
import gt.edu.miumg.petstore.use_cases.PetUseCases.PetUseCases
import gt.edu.miumg.petstore.util.Response
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PetViewModel @Inject constructor(
    private val petUseCases: PetUseCases,
) : ViewModel() {
    private val _getPets = mutableStateOf<Response<PetState?>>(Response.Success(null))
    val getPets: State<Response<PetState?>> = _getPets

    private val _getFood = mutableStateOf<Response<PetState?>>(Response.Success(null))
    val getFood: State<Response<PetState?>> = _getFood

    private val _getAccessory = mutableStateOf<Response<PetState?>>(Response.Success(null))
    val getAccessory: State<Response<PetState?>> = _getAccessory

    fun getPetInfo() {
        viewModelScope.launch {
            petUseCases.getPets().collect {
                _getPets.value = it
            }
        }
    }

    fun getFoodInfo() {
        viewModelScope.launch {
            petUseCases.getFood().collect {
                _getFood.value = it
            }
        }
    }

    fun getAccessoryInfo() {
        viewModelScope.launch {
            petUseCases.getAccessory().collect {
                _getAccessory.value = it
            }
        }
    }
}