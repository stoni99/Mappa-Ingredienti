package com.example.mappaingredienti

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class StepRicettaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step_ricetta)
        val recipeStep = recipeStep
        val textView_step = findViewById<TextView>(R.id.textview_step)
        textView_step.text = recipeStep
    }


    //mi riprendo il dato della ricetta
    private val recipeStep: String?
    private get() {
        //mi riprendo il dato della ricetta
        val i = intent
        return i.getStringExtra("STEP")
    }
}