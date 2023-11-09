package gt.edu.miumg.petstore.repository

import gt.edu.miumg.petstore.models.SearchState
import gt.edu.miumg.petstore.util.Response
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun getAllSearch(query: String): Flow<Response<SearchState>>
    fun getSearchByCollection(collection: String, query: String): Flow<Response<SearchState>>
}