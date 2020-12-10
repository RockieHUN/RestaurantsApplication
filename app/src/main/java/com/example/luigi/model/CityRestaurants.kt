package com.example.luigi.model

data class CityRestaurants(
        /*val total_entries : Int,
        val per_page : Int,
        var current_page : Int,
        val restaurants : List<Restaurant>*/
        val total_entries : Int,
        val page : Int,
        val per_page : Int,
        val restaurants : List<Restaurant>
)