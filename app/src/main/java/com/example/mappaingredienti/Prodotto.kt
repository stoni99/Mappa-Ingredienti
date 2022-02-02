package com.example.mappaingredienti

class Prodotto {
    var lng: String? = null
    var lat: String? = null
    var producer: String? = null

    constructor() {}
    constructor(lng: String?, lat: String?, producer: String?) {
        this.lng = lng
        this.lat = lat
        this.producer = producer
    }
}