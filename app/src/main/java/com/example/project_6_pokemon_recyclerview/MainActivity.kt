package com.example.project_6_pokemon_recycler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.project_6_pokemon_recyclerview.PokemonAdapter
import okhttp3.Headers

data class Pokemon(val name: String, val image: String, val type: String)


class MainActivity : AppCompatActivity() {

    private lateinit var pokemonsList: MutableList<Pokemon>
    private lateinit var recyclerViewPokemons: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerViewPokemons = findViewById(R.id.pokemonsRecyclerView)
        pokemonsList = mutableListOf()

        recyclerViewPokemons.adapter = PokemonAdapter(pokemonsList)

        recyclerViewPokemons.layoutManager = LinearLayoutManager(this)

        getPokemonDetails()
    }

    private fun getPokemonDetails() {
        val client = AsyncHttpClient()
        val apiUrl = "https://pokeapi.co/api/v2/pokemon"

        client.get(apiUrl, object : JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Headers,
                json: JsonHttpResponseHandler.JSON
            ) {
                Log.d("Pokemon", "Response successful: $json")

                val results = json.jsonObject.optJSONArray("results")
                if (results != null) {
                    for (i in 0 until results.length()) {
                        val pokemonJson = results.getJSONObject(i)
                        val name = pokemonJson.getString("name")
                        val url = pokemonJson.getString("url")

                        getPokemonDetailsByUrl(name, url)
                    }
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Pokemon Error", errorResponse)
            }
        })
    }

    private fun getPokemonDetailsByUrl(name: String, url: String) {
        val client = AsyncHttpClient()

        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Headers,
                json: JsonHttpResponseHandler.JSON
            ) {
                Log.d("Pokemon Details", "Response successful: $json")

                val imageUrl = json.jsonObject
                    .getJSONObject("sprites")
                    .getString("front_default")

                val typesArray = json.jsonObject.getJSONArray("types")
                val typesList = mutableListOf<String>()
                for (j in 0 until typesArray.length()) {
                    val type = typesArray.getJSONObject(j)
                        .getJSONObject("type")
                        .getString("name")
                    typesList.add(type)
                }

                pokemonsList.add(Pokemon(name, imageUrl, typesList.joinToString(", ")))

                // Notify the adapter that the data has changed
                recyclerViewPokemons.adapter?.notifyDataSetChanged()
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Pokemon Details Error", errorResponse)
            }
        })
    }
}