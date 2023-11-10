package gt.edu.miumg.petstore.di

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gt.edu.miumg.petstore.data.CartRepositoryImpl
import gt.edu.miumg.petstore.data.FavoriteRepositoryImpl
import gt.edu.miumg.petstore.data.PetRepositoryImpl
import gt.edu.miumg.petstore.data.SearchRepositoryImpl
import gt.edu.miumg.petstore.repository.CartRepository
import gt.edu.miumg.petstore.repository.FavoriteRepository
import gt.edu.miumg.petstore.repository.PetRepository
import gt.edu.miumg.petstore.repository.SearchRepository
import gt.edu.miumg.petstore.use_cases.CartUseCases.CartUseCases
import gt.edu.miumg.petstore.use_cases.CartUseCases.GetCart
import gt.edu.miumg.petstore.use_cases.CartUseCases.SetCart
import gt.edu.miumg.petstore.use_cases.FavoriteUseCases.DeleteFavorites
import gt.edu.miumg.petstore.use_cases.FavoriteUseCases.FavoriteUseCases
import gt.edu.miumg.petstore.use_cases.FavoriteUseCases.GetFavorites
import gt.edu.miumg.petstore.use_cases.FavoriteUseCases.SetFavorite
import gt.edu.miumg.petstore.use_cases.PetUseCases.GetAccessory
import gt.edu.miumg.petstore.use_cases.PetUseCases.GetFood
import gt.edu.miumg.petstore.use_cases.PetUseCases.GetPets
import gt.edu.miumg.petstore.use_cases.PetUseCases.PetUseCases
import gt.edu.miumg.petstore.use_cases.SearchUseCases.GetAllSearch
import gt.edu.miumg.petstore.use_cases.SearchUseCases.GetSearchByCollection
import gt.edu.miumg.petstore.use_cases.SearchUseCases.SearchUseCases
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
        getPets = GetPets(repository),
        getFood = GetFood(repository),
        getAccessory = GetAccessory(repository)
    )

    @Singleton
    @Provides
    fun provideCartRepository(firebaseFirestore: FirebaseFirestore): CartRepository {
        return CartRepositoryImpl(firebaseFirestore)
    }

    @Singleton
    @Provides
    fun provideCartUseCases(repository: CartRepository) = CartUseCases(
        getCart = GetCart(repository),
        setCart = SetCart(repository),
    )

    @Singleton
    @Provides
    fun provideSearchRepository(firebaseFirestore: FirebaseFirestore): SearchRepository {
        return SearchRepositoryImpl(firebaseFirestore)
    }

    @Singleton
    @Provides
    fun provideSearchUseCases(repository: SearchRepository) = SearchUseCases(
        getAllSearch = GetAllSearch(repository),
        getSearchByCollection = GetSearchByCollection(repository)
    )

    @Singleton
    @Provides
    fun provideFavoriteRepository(firebaseFirestore: FirebaseFirestore): FavoriteRepository {
        return FavoriteRepositoryImpl(firebaseFirestore)
    }

    @Singleton
    @Provides
    fun provideFavoriteUseCases(repository: FavoriteRepository) = FavoriteUseCases(
        getFavorites = GetFavorites(repository),
        setFavorite = SetFavorite(repository),
        deleteFavorites = DeleteFavorites(repository)
    )
}