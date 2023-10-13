package gt.edu.miumg.petstore

import androidx.annotation.StringRes

sealed class Screen (val route: String, @StringRes val resourceId: Int) {
    object Home : Screen("home", R.string.home)
    object PetCatalog : Screen("petcatalog", R.string.pet_catalog)
    object FoodCatalog : Screen("foodcatalog", R.string.food_catalog)
    object AccessoriesCatalog : Screen("accessoriescatalog", R.string.accessories_catalog)
}