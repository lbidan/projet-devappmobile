package com.example.miameirb

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var idMeal: String? = null
    var nameTextView: TextView = itemView.findViewById(R.id.textview_name_recipe)
    var imgView: ImageView = itemView.findViewById(R.id.imageview_recipe)
    var button: Button = itemView.findViewById(R.id.button_recipe)

    init {
        button.setOnClickListener {
            val intent = Intent(itemView.context, MealActivity::class.java)
            intent.putExtra("meal", this.idMeal)
            itemView.context.startActivity(intent)
        }
    }
}

class RecipesAdapter(val recipes: List<Recipe>): RecyclerView.Adapter<RecipeViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recipe_item, parent, false)
        return RecipeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.idMeal = recipes.get(position).idMeal
        holder.nameTextView.text = recipes.get(position).strMeal
        holder.imgView.setImageBitmap(recipes.get(position).strMealThumb?.let {
            getBitmapFromURL(
                it
            )
        })
    }

    override fun getItemCount(): Int {
        return recipes.count()
    }
}