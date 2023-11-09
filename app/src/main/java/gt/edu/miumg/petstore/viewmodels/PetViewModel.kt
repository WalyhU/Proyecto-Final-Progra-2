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

    fun getPetInfo() {
        viewModelScope.launch {
            petUseCases.getPets().collect {
                _getPets.value = it
            }
        }
    }
}