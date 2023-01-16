package com.example.miameirb

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import android.widget.ImageView

import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class MealActivity: AppCompatActivity() {
    private lateinit var titleView: TextView
    private lateinit var imageView: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var instructionView: TextView
    private lateinit var youtubeView: TextView

    private fun getBitmapFromURL(src: String): Bitmap? {
        return try {
            val url = URL(src)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            // Log exception
            Log.e("EXCEPTION", e.message.toString())
            null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal)
        var meal = intent.getStringExtra("meal")
        if (meal != null) {
            Log.d("MEAL", meal)
        }

        val policy: ThreadPolicy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        titleView = findViewById(R.id.textview_meal_name)
        imageView = findViewById(R.id.imageview_meal)
        recyclerView = findViewById(R.id.recyclerview_meal_ingredients)
        instructionView = findViewById(R.id.textview_meal_instructions)
        youtubeView = findViewById(R.id.textview_ytb_link)

        val url = URL("https://www.themealdb.com/api/json/v1/1/lookup.php?i=$meal")
        val request = Request.Builder()
            .url(url)
            .build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                e.localizedMessage?.let { Log.e("OKHTTP", it) }
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let {
                    val gson = Gson()
                    val mealResponse = gson.fromJson(it, MealResponse::class.java)
                    
                    if (mealResponse.meals != null) {
                        runOnUiThread {
                            val meal = mealResponse.meals!![0]
                            Log.d("MEAL", meal.toString())
                            titleView.text = meal.strMeal
                            instructionView.text = meal.strInstructions
                            val bitmap = meal.strMealThumb?.let { it1 -> getBitmapFromURL(it1) }
                            imageView.setImageBitmap(bitmap)
                            youtubeView.text =meal.strYoutube

                            val ingredients = mutableListOf<Ingredient>()
                            for (i in 1..20) {
                                val ingredient = meal["strIngredient", i]
                                val measure = meal["strMeasure", i]
                                if (ingredient != null) {
                                    val ingredientObj: Ingredient = Ingredient(ingredient, measure)
                                    ingredients.add(ingredientObj)
                                }
                            }

                            recyclerView.layoutManager = LinearLayoutManager(applicationContext)
                            recyclerView.adapter = IngredientAdapter(ingredients)
                        }
                    }

                    Log.d("OKHTTP", "Got ${mealResponse.meals?.count()} results")
                }
            }
        })
    }
}