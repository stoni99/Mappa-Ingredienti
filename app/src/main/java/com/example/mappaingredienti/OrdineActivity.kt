package com.example.mappaingredienti

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat

class OrdineActivity : AppCompatActivity() {
    private var textViewTitle: TextView? = null
    private var editTextPeopleNumber: EditText? = null
    private var textViewResult: TextView? = null
    private var textViewAddress: TextView? = null
    private var btnContinue: Button? = null
    private var recipe: Ricetta? = null
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ordine)
        getRecipe()
        textViewTitle = findViewById(R.id.textview_titolo)
        editTextPeopleNumber = findViewById(R.id.edittext_people)
        textViewResult = findViewById(R.id.textview_risultato)
        textViewAddress = findViewById(R.id.textview_indirizzo)
        btnContinue = findViewById(R.id.btn_continua)
        textViewTitle.setText("""
    Stai ordinando:
    ${recipe?.getName()}
    """.trimIndent())
        setButtonEnable(false)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun getRecipe() {
        //mi riprendo il dato della ricetta
        val i = intent
        recipe = i.getSerializableExtra("RECIPE") as Ricetta?
    }

    fun calculateIngredients(v: View?) {
        var finalIngredientsQuantity = ""
        val numberOfPeopleString = editTextPeopleNumber!!.text.toString()
        if (numberOfPeopleString == "0" || numberOfPeopleString.isEmpty()) {
            editTextPeopleNumber!!.error = "Inserisci il numero di persone"
            editTextPeopleNumber!!.requestFocus()
            return
        }
        val numberOfPeople: Int = numberOfPeopleString.toInt()
        for ((name, quantity) in recipe.getIngredients().entrySet()) {
            finalIngredientsQuantity = if (quantity == "qb") {
                "$finalIngredientsQuantity\u25CF $name, qb\n"
            } else {
                val qt: Double = quantity.split(" ").toTypedArray().get(0).trim { it <= ' ' }.toDouble()
                val type: String = quantity.split(" ").toTypedArray().get(1).trim { it <= ' ' }
                val result = qt * numberOfPeople
                val df = DecimalFormat("###.##")
                """$finalIngredientsQuantity● $name, ${df.format(result)} $type
"""
            }
        }
        textViewResult!!.text = finalIngredientsQuantity
        setButtonEnable(true)
    }

    private fun setButtonEnable(isEnabled: Boolean) {
        btnContinue!!.isClickable = isEnabled
        btnContinue!!.isEnabled = isEnabled
    }

    fun openFinishActivity(v: View?) {
        if (textViewResult!!.text == "-") {
            return
        }
        val i = Intent(this, FinaleActivity::class)
        i.putExtra("RECIPE", recipe)
        startActivity(i)
    }
}