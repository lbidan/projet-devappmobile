package com.example.miameirb

import com.google.gson.annotations.SerializedName

class CategoriesResponse {
    var count: Int? = null
    var previous: String? = null
    var next: String? = null

    @SerializedName("categories")
    var categories: List<Category>? = null
}

class Category {

    var idCategory: String? = null
    var strCategory: String? = null
    var strCategoryThumb: String? = null
    var strCategoryDescription: String? = null

}