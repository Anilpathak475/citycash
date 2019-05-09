package com.citycash.shop

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.citycash.shop.databinding.FragmentProductBinding
import com.citycash.shop.network.errorhandler.WishErrorHandler
import com.citycash.shop.network.model.Product
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_product.*
import kotlinx.android.synthetic.main.item_product.view.*
import kotlin.properties.Delegates


class ProductFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    private val productAdapter by lazy {
        ProductAdapter {
            val action = ProductFragmentDirections.actionProductFragmentToProductDetailFragment(it)
            navController.navigate(action)
        }
    }
    lateinit var binding: FragmentProductBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductBinding.inflate(inflater, container, false)
        binding.productRecyclerView.adapter = productAdapter
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        productRecyclerView.let {
            it.layoutManager = LinearLayoutManager(context)
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
                Toast.makeText(activity, exception.message, Toast.LENGTH_LONG).show()
            }
            is WishErrorHandler.ErrorConfig.AlbumException -> {
                Toast.makeText(activity, exception.message, Toast.LENGTH_LONG).show()
            }
            else -> Toast.makeText(activity, "Oops! Something went wrong.", Toast.LENGTH_LONG).show()
        }

    }

    private fun setData(products: List<Product>) {
        binding.hasProducts = products.isNotEmpty()

        productAdapter.products = products
        productAdapter.notifyDataSetChanged()
    }


    inner class ProductAdapter(val listener: (String) -> Unit) :
        RecyclerView.Adapter<ProductAdapter.ProductViewHolder>(), AutoUpdatableAdapter {

        var products: List<Product> by Delegates.observable(emptyList()) { _, old, new ->
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
                itemView.setOnClickListener {
                    listener(product.id)
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment ProductFragment.
         */
        @JvmStatic
        fun newInstance(param1: Int) =
            ProductFragment().apply {
                arguments = Bundle().apply {
                    putInt(NAV_ACTION_PARAM, param1)
                }
            }
    }


}

