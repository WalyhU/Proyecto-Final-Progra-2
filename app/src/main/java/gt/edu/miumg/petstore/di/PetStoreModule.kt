package gt.edu.miumg.petstore.di

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gt.edu.miumg.petstore.data.PetRepositoryImpl
import gt.edu.miumg.petstore.repository.PetRepository
import gt.edu.miumg.petstore.use_cases.GetPets
import gt.edu.miumg.petstore.use_cases.PetUseCases
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PetStoreModule {
    @Singleton
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Singleton
    @Provides
    fun providePetRepository(firebaseFirestore: FirebaseFirestore): PetRepository {
        return PetRepositoryImpl(firebaseFirestore)
    }

    @Singleton
    @Provides
    fun providePetUseCases(repository: PetRepository) = PetUseCases(
        getPets = GetPets(repository)
    )
}