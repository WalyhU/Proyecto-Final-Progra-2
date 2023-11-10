package gt.edu.miumg.petstore.use_cases.PetUseCases

import gt.edu.miumg.petstore.repository.PetRepository
import javax.inject.Inject

class GetAccessory @Inject constructor(
    private val repository: PetRepository
) {
    operator fun invoke() =
        repository.getAccessory()
}