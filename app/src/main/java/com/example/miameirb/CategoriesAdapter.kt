package com.example.miameirb

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.content.Intent
import android.content.Context
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

class CategoriesAdapter(private val categories: List<Category>): RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    inner class CategoriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameTextView: TextView = itemView.findViewById(R.id.textview_name_category)
        var imgView: ImageView = itemView.findViewById(R.id.imageview_category)
        var button: Button = itemView.findViewById(R.id.button_category)

        init {
            button.setOnClickListener {
                val intent = Intent(itemView.context, RecipesActivity::class.java)
                intent.putExtra("category", nameTextView.text)
                itemView.context.startActivity(intent)
            }
        }
    }

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
        return CategoriesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        holder.nameTextView.text = categories.get(position).strCategory
        holder.imgView.setImageBitmap(categories.get(position).strCategoryThumb?.let {
            getBitmapFromURL(
                it
            )
        })
    }

    override fun getItemCount(): Int {
        return categories.count()
    }
}