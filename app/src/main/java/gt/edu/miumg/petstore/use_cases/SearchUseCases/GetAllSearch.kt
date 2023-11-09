package gt.edu.miumg.petstore.use_cases.SearchUseCases

import gt.edu.miumg.petstore.repository.SearchRepository
import javax.inject.Inject

class GetAllSearch @Inject constructor(
    private val repository: SearchRepository
) {
    operator fun invoke(query: String) =
        repository.getAllSearch(query = query)
}