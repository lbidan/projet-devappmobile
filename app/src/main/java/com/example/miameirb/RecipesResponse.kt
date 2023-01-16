package com.example.miameirb

import com.google.gson.annotations.SerializedName

class RecipesResponse {
    var count: Int? = null
    var previous: String? = null
    var next: String? = null

    @SerializedName("meals")
    var recipes: List<Recipe>? = null
}

class Recipe {

    var idMeal: String? = null
    var strMeal: String? = null
    var strMealThumb: String? = null

}