package gt.edu.miumg.petstore.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gt.edu.miumg.petstore.models.SearchState
import gt.edu.miumg.petstore.use_cases.SearchUseCases.SearchUseCases
import gt.edu.miumg.petstore.util.Response
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCases: SearchUseCases
) : ViewModel() {
    private val _getAllSearch = mutableStateOf<Response<SearchState>>(Response.Success(null))
    val getAllSearch : State<Response<SearchState>> = _getAllSearch

    private val _getSearchByCollection = mutableStateOf<Response<SearchState>>(Response.Success(null))
    val getSearchByCollection : State<Response<SearchState>> = _getSearchByCollection

    fun getAllSearch(query: String) {
        viewModelScope.launch {
            searchUseCases.getAllSearch(query).collect {
                _getAllSearch.value = it
            }
        }
    }

    fun getSearchByCollection(collection: String, query: String) {
        viewModelScope.launch {
            searchUseCases.getSearchByCollection(collection, query).collect {
                _getSearchByCollection.value = it
            }
        }
    }
}