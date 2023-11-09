package gt.edu.miumg.petstore.use_cases.SearchUseCases

import gt.edu.miumg.petstore.repository.SearchRepository
import javax.inject.Inject

class GetSearchByCollection @Inject constructor(
    private val repository: SearchRepository
) {
    operator fun invoke(query: String, collection: String) =
        repository.getSearchByCollection(query = query, collection = collection)
}