package com.example.mappaingredienti

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment


class MapActivity : FragmentActivity(), OnMapReadyCallback {
    private var recipe: Ricetta? = null
    private var googleMap: GoogleMap? = null
    private var textViewTotalCO2: TextView? = null
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private val productList: MutableList<Product> = ArrayList()
    private var userPosition: LatLng? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        textViewTotalCO2 = findViewById(R.id.textview_co2)
        getRecipe()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val mapFragment: SupportMapFragment? = supportFragmentManager.findFragmentById(R.id.recipemap) as SupportMapFragment?
        if (mapFragment != null) {
            mapFragment.getMapAsync(this)
        }
    }

    private fun getRecipe() {
        //mi riprendo il dato della ricetta
        val i = intent
        recipe = i.getSerializableExtra("RECIPE") as Ricetta?
    }

    fun onMapReady(googleMap: GoogleMap) {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        this.googleMap = googleMap
        for (ingredient in recipe.getIngredients().keySet()) {
            db.collection("Product").document(ingredient.toLowerCase()).get().addOnCompleteListener(OnCompleteListener<Any> { task ->
                if (task.isSuccessful) {
                    val document: DocumentSnapshot = task.result
                    if (document.exists()) {
                        val product: Prodotto = document.toObject(Prodotto::class)
                        productList.add(product)
                        createMarker(ingredient, product, googleMap)
                        Log.v("PRODUCT", product.getProducer())
                    } else {
                        Log.v("PRODUCT", ingredient + "not exists")
                    }
                }
            })
        }
    }

    private fun createMarker(ingredient: String, product: Prodotto, googleMap: GoogleMap) {
        val prod = LatLng(product.getLat().toDouble(), product.getLng().toDouble())
        googleMap.addMarker(MarkerOptions().position(prod).title(product.getProducer().toString() + ": " + ingredient))
        val cameraPosition: CameraPosition = Builder().target(prod).zoom(10).build()
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    fun findUserAddress(v: View?) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@MapActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 44)
            return
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(OnCompleteListener<Location?> { task ->
            val location = task.result
            if (location != null) {
                val geocoder = Geocoder(this@MapActivity, Locale.getDefault())
                try {
                    val addressList = geocoder.getFromLocation(
                        location.latitude, location.longitude, 1)
                    userPosition = LatLng(addressList[0].latitude, addressList[0].longitude)
                    googleMap.addMarker(MarkerOptions().position(userPosition).title("La tua posizione").icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
                    val cameraPosition: CameraPosition = Builder().target(userPosition).zoom(7).build()
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        })
    }

    fun calculateCO2Emissions(v: View?) {
        if (userPosition == null) {
            findUserAddress(this.currentFocus)
            return
        }

        // Controllo se la lista dei prodotti non è vuota
        if (productList.isEmpty()) {
            return
        }
        var totalCO2emitted = 0f
        // Trovo la distanza per ogni prodotto
        for (product in productList) {
            val userLocation = Location("")
            val productLocation = Location("")
            userLocation.latitude = userPosition.latitude
            userLocation.longitude = userPosition.longitude
            productLocation.latitude = product.getLat().toDouble()
            productLocation.longitude = product.getLng().toDouble()
            val distanceInKm = userLocation.distanceTo(productLocation) / 1000
            val co2emitted = distanceInKm * 260 / 100
            totalCO2emitted += co2emitted
        }
        textViewTotalCO2!!.text = "Totale CO2 emessa: $totalCO2emitted g/Km"
        textViewTotalCO2!!.visibility = View.VISIBLE
    }
}