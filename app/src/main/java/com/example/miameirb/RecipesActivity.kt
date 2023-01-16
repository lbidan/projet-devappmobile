package com.example.miameirb

import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.net.URL

class RecipesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecipesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipes)
        var category = intent.getStringExtra("category")

        val policy: ThreadPolicy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        recyclerView = findViewById(R.id.recycler_view)

        val url = URL("https://www.themealdb.com/api/json/v1/1/filter.php?c=$category")
        val request = Request.Builder()
            .url(url)
            .build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                e.localizedMessage?.let { Log.e("OKHTTP", it) }
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let {
                    val gson = Gson()
                    val recipesResponse = gson.fromJson(it, RecipesResponse::class.java)
                    recipesResponse.recipes?.let { it1 ->
                        runOnUiThread {
                            adapter = RecipesAdapter(it1)
                            recyclerView.adapter = adapter
                            recyclerView.layoutManager = LinearLayoutManager(applicationContext)
                        }

                    }
                    Log.d("OKHTTP", "Got ${recipesResponse.recipes?.count()} results")
                }
            }
        })}
    }