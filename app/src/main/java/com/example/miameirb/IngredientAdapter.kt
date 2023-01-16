package com.example.miameirb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class IngredientAdapter(private val ingredients: MutableList<Ingredient>): RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>() {

    inner class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ingredientTextView: TextView = itemView.findViewById(R.id.textview_ingredient)
        var measureTextView: TextView = itemView.findViewById(R.id.textview_measure)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.ingredient_item, parent, false)
        return IngredientViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        holder.ingredientTextView.text = ingredients[position].ingredient
        holder.measureTextView.text = ingredients[position].measure
    }

    override fun getItemCount() = ingredients.size
}