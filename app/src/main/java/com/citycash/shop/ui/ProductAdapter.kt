package com.citycash.shop.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.citycash.shop.AutoUpdatableAdapter
import com.citycash.shop.R
import com.citycash.shop.gone
import com.citycash.shop.network.model.Product
import com.citycash.shop.visible
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_product.view.*


class ProductAdapter(val listener: (Product) -> Unit) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>(),
    AutoUpdatableAdapter, Filterable {
    val products = MutableLiveData<List<Product>>()
    override fun getFilter(): Filter {
        return object : Filter() {
            val filteredList = ArrayList<Product>()
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                filteredList.clear()
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    filteredProducts = products.value!!
                } else {
                    for (row in products.value!!) {
                        if (row.name!!.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }
                    filteredProducts = filteredList
                }
                return FilterResults()
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                notifyDataSetChanged()
            }
        }
    }

    var filteredProducts: List<Product> = ArrayList()

    init {
        products.observeForever {
            this.filteredProducts = it
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ProductViewHolder {
        val layoutInflater =
            viewGroup.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val convertView = layoutInflater.inflate(com.citycash.shop.R.layout.item_product, viewGroup, false)
        return ProductViewHolder(convertView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(filteredProducts[position])
    }

    override fun getItemCount(): Int {
        return filteredProducts.size
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(product: Product) {
            itemView.let {
                it.productName.text = if (product.name!!.isNotEmpty()) product.name else ""
                it.productId.text = if (product.id!!.isNotEmpty()) product.id else ""
                val price =
                    """${it.context.getString(R.string.Rs)}${if (product.price!!.isNotEmpty()) product.price else ""}"""
                it.productPrice.text = price
                it.productQty.text = if (product.qty!!.isNotEmpty()) product.qty else ""
                if (!it.productImageLoader.isVisible) it.productImageLoader.visible()
                if (product.image!!.isNotEmpty())
                    Picasso.get().load(product.image)
                        .into(it.productImage, object : Callback {
                            override fun onSuccess() {
                                it.productImage.visible()
                                it.productImageLoader.gone()
                            }

                            override fun onError(e: java.lang.Exception?) {
                                it.productImageLoader.gone()
                            }
                        })

                it.setOnClickListener {
                    listener(product)
                }
            }
        }
    }
}