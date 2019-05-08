package com.citycash.shop

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.citycash.shop.network.errorhandler.WishErrorHandler
import com.citycash.shop.network.model.Product
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_product.view.*
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    private val productAdapter by lazy { ProductAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        productRecyclerView.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = productAdapter
        }

        viewModel.products.observe(this
            , Observer { setData(it) })
        viewModel.exception.observe(this, Observer { errorHandler(it) })
        viewModel.loadProducts()
    }

    private fun errorHandler(exception: Exception) {
        when (exception) {
            is WishErrorHandler.ErrorConfig.NetworkException -> {
                Toast.makeText(this@MainActivity, exception.message, Toast.LENGTH_LONG).show()
            }
            is WishErrorHandler.ErrorConfig.AlbumException -> {
                Toast.makeText(this@MainActivity, exception.message, Toast.LENGTH_LONG).show()
            }
            else -> Toast.makeText(this@MainActivity, "Oops! Something went wrong.", Toast.LENGTH_LONG).show()
        }
        progressBar.gone()

    }
    private fun setData(words: List<Product>) {
        productAdapter.products = words
        productAdapter.notifyDataSetChanged()
        progressBar.gone()
        productRecyclerView.visible()
    }

    class ProductAdapter :
        RecyclerView.Adapter<ProductAdapter.ProductViewHolder>(), AutoUpdatableAdapter {

        var products: List<Product> by Delegates.observable(emptyList()) { prop, old, new ->
            autoNotify(old, new) { o, n -> o.id == n.id }
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ProductViewHolder {
            val layoutInflater =
                viewGroup.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val convertView = layoutInflater.inflate(R.layout.item_product, viewGroup, false)
            return ProductViewHolder(convertView)
        }

        override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
            holder.bind(products[position])
        }

        override fun getItemCount(): Int {
            return products.size
        }

        inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(product: Product) {
                itemView.productName.text = product.name
                itemView.productId.text = product.id
                itemView.productPrice.text = product.price
                itemView.productQty.text = product.qty
                if (!itemView.productImageLoader.isVisible) itemView.productImageLoader.visible()
                if (product.image.isNotEmpty())
                    Picasso.get().load("http://i.imgur.com/DvpvklR.png")
                        .into(itemView.productImage, object : Callback {
                            override fun onSuccess() {
                                itemView.productImage.visible()
                                itemView.productImageLoader.gone()
                            }

                            override fun onError(e: java.lang.Exception?) {
                                itemView.productImageLoader.gone()
                            }
                        })
            }
        }
    }
}
