package com.example.mappaingredienti

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.IOException
import java.util.*

class FinaleActivity : AppCompatActivity() {
    //mi riprendo il dato della ricetta
    private var recipe: Ricetta? = null
        private get() {
            //mi riprendo il dato della ricetta
            val i = intent
            field = i.getSerializableExtra("RECIPE") as Ricetta?
        }
    private var textViewAddress: TextView? = null
    private var textViewAcquista: TextView? = null
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)
        recipe
        textViewAddress = findViewById(R.id.textview_indirizzo)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        textViewAcquista = findViewById(R.id.textViewAcquista)
    }

    fun findShippingAddress(v: View?) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@FinaleActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 44)
            return
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(OnCompleteListener<Location?> { task ->
            val location = task.result
            if (location != null) {
                val geocoder = Geocoder(this@FinaleActivity, Locale.getDefault())
                try {
                    val addressList = geocoder.getFromLocation(
                        location.latitude, location.longitude, 1)
                    textViewAddress!!.text = """
                        Indirizzo di spedizione:
                        ${addressList[0].getAddressLine(0)}
                        """.trimIndent()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        })
    }

    fun buy(v: View?) {
        textViewAcquista!!.text = "L'ordine verra' elaborato entro 24h"
    }

    fun cancel(v: View?) {
        val i = Intent(this, MainActivity::class)
        startActivity(i)
    }
}