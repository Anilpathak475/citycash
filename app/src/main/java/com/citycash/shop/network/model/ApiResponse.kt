package com.citycash.shop.network.model


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class SortProps(
    @SerializedName("A")
    val a: Int = 0,
    @SerializedName("B")
    val b: Int = 0,
    @SerializedName("C")
    val c: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(a)
        parcel.writeInt(b)
        parcel.writeInt(c)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SortProps> {
        override fun createFromParcel(parcel: Parcel): SortProps {
            return SortProps(parcel)
        }

        override fun newArray(size: Int): Array<SortProps?> {
            return arrayOfNulls(size)
        }
    }
}


data class Product(
    @SerializedName("image")
    val image: String?,
    @SerializedName("sort_props")
    val sortProps: SortProps?,
    @SerializedName("category_name")
    val categoryName: String?,
    @SerializedName("category_id")
    val categoryId: String?,
    @SerializedName("price")
    val price: String?,
    @SerializedName("qty")
    val qty: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readParcelable(SortProps::class.java.classLoader),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(image)
        parcel.writeParcelable(sortProps, flags)
        parcel.writeString(categoryName)
        parcel.writeString(categoryId)
        parcel.writeString(price)
        parcel.writeString(qty)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}


data class ApiResponse(
    @SerializedName("data")
    val data: List<Product>?
)


