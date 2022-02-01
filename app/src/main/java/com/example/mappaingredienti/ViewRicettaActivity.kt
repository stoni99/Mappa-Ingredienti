package com.example.mappaingredienti

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RecipeViewerActivity : AppCompatActivity() {
    private var recipe: Ricetta? = null
    private var textview_ingredient: TextView? = null
    private var img_recipe: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_ricetta)
        textview_ingredient = findViewById(R.id.textview_ingredients)
        img_recipe = findViewById(R.id.imageview_ricetta)
        getRicetta()
        setRicetta()
    }

    private fun getRicetta() {
        //mi riprendo il dato della ricetta
        val i = intent
        recipe = i.getSerializableExtra("RECIPE") as Ricetta?
    }

    private fun setRicetta() {
        val name = findViewById<TextView>(R.id.textview_nome_ricetta)
        val recipeName: String = recipe.getName()
        name.setText(recipeName.substring(0, 1).toUpperCase() + recipeName.substring(1))
        Picasso.get().load(recipe.getImg()).into(img_recipe)
        textview_ingredient.setText(recipe.getIngredientsString(recipe.getIngredients()))
    }

    fun openStepActivity(v: View?) {
        val i = Intent(this, StepRicettaActivity::class)
        i.putExtra("STEP", recipe.getStep())
        startActivity(i)
    }

    fun openOrderActivity(v: View?) {
        val i = Intent(this, OrdineActivity::class)
        i.putExtra("RECIPE", recipe)
        startActivity(i)
    }

    fun openMapActivity(v: View?) {
        val i = Intent(this, MapActivity::class)
        i.putExtra("RECIPE", recipe)
        startActivity(i)
    }
}
