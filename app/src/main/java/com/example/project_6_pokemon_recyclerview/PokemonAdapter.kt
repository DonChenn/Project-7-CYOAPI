package com.example.project_6_pokemon_recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project_6_pokemon_recycler.Pokemon
import com.example.project_6_pokemon_recycler.R

class PokemonAdapter(val pokemonList: MutableList<Pokemon>) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    class PokemonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val pokemonImage: ImageView

        init {
            pokemonImage = view.findViewById(R.id.pokemon_image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pokemon_item, parent, false)
        return PokemonViewHolder(view)
    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val currentPokemon = pokemonList[position]
        Glide.with(holder.itemView)
            .load(currentPokemon.image)
            .centerCrop()
            .into(holder.pokemonImage)
        holder.itemView.findViewById<TextView>(R.id.pokemonName).text = currentPokemon.name
        holder.itemView.findViewById<TextView>(R.id.pokemonType).text = currentPokemon.type
    }
}
