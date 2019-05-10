package com.citycash.shop.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.citycash.shop.databinding.FragmentProductDetailBinding
import com.citycash.shop.gone
import com.citycash.shop.network.model.Product
import com.citycash.shop.visible
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_product_detail.*


class ProductDetailFragment : Fragment() {
    private var product: Product? = null
    private lateinit var binding: FragmentProductDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey("product"))
                product = it.getParcelable("product") as Product
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (product != null) {
            binding.product = product!!
            if (product!!.image!!.isNotEmpty())
                Picasso.get().load(product!!.image)
                    .into(productDetailmage, object : Callback {
                        override fun onSuccess() {
                            productDetailmage.visible()
                            productDeailImageLoader.gone()
                        }

                        override fun onError(e: Exception?) {
                            productDeailImageLoader.gone()
                        }
                    })
        }
    }

}
