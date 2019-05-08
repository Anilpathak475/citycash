package com.citycash.shop.network.model


import com.google.gson.annotations.SerializedName

data class SortProps(@SerializedName("A")
                     val a: Int = 0,
                     @SerializedName("B")
                     val b: Int = 0,
                     @SerializedName("C")
                     val c: Int = 0)


data class Product(@SerializedName("image")
                    val image: String = "",
                   @SerializedName("sort_props")
                    val sortProps: SortProps,
                   @SerializedName("category_name")
                    val categoryName: String = "",
                   @SerializedName("category_id")
                    val categoryId: String = "",
                   @SerializedName("price")
                    val price: String = "",
                   @SerializedName("qty")
                    val qty: String = "",
                   @SerializedName("name")
                    val name: String = "",
                   @SerializedName("description")
                    val description: String = "",
                   @SerializedName("id")
                    val id: String = "")


data class ApiResponse(@SerializedName("data")
                       val data: List<Product>?)


