package gt.edu.miumg.petstore.use_cases

import gt.edu.miumg.petstore.repository.PetRepository
import javax.inject.Inject

class GetPets @Inject constructor(
    private val repository: PetRepository
) {
    operator fun invoke() =
        repository.getPet()
}