package com.example.mappaingredienti

import java.io.Serializable
import java.util.*

class Ricetta : Serializable {
    var name: String? = null
    var description: String? = null
    var step: String? = null
    var img: String? = null
    var ingredients: HashMap<String, String>? = null

    constructor(name: String?, description: String?, step: String?, img: String?, ingredients: HashMap<String, String>?) {
        this.name = name
        this.description = description
        this.step = step
        this.img = img
        this.ingredients = ingredients
    }

    constructor() {}

    fun getIngredientsString(ingredients: HashMap<String, String?>): String {
        val ingredients_string = StringBuilder()
        for (ingredientName in ingredients.keys) {
            val name: String = ingredientName.substring(0, 1).toUpperCase() + ingredientName.substring(1)
            ingredients_string.append("\u25CF ").append(name).append(", ").append(ingredients[ingredientName]).append("\n")
        }
        return ingredients_string.toString()
    }
}
