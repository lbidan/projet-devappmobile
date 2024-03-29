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

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var categoriesAdapter: CategoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val policy: ThreadPolicy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        recyclerView = findViewById(R.id.recycler_view)

        val url = URL("https://www.themealdb.com/api/json/v1/1/categories.php")
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
                    val categoriesResponse = gson.fromJson(it, CategoriesResponse::class.java)
                    categoriesResponse.categories?.let { it1 ->
                        runOnUiThread {
                            categoriesAdapter = CategoriesAdapter(it1)
                            recyclerView.adapter = categoriesAdapter
                            recyclerView.layoutManager = LinearLayoutManager(applicationContext)
                        }

                    }
                    Log.d("OKHTTP", "Got ${categoriesResponse.categories?.count()} results")
                }
            }
        })
    }
}