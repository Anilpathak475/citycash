package com.citycash.shop.ui.fragment

import android.R
import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.citycash.shop.FilterEvent
import com.citycash.shop.databinding.FragmentProductBinding
import com.citycash.shop.network.errorhandler.WishErrorHandler
import com.citycash.shop.network.model.Product
import com.citycash.shop.ui.ProductAdapter
import com.citycash.shop.ui.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_product.*
import kotlinx.android.synthetic.main.layout_sort.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class ProductFragment : Fragment() {

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onFilter(filterEvent: FilterEvent) {
        productAdapter.filter.filter(filterEvent.filterQuery)
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    private val productAdapter by lazy {
        ProductAdapter {
            val action = ProductFragmentDirections.actionProductFragmentToProductDetailFragment(it)
            navController.navigate(action)
        }
    }
    private lateinit var binding: FragmentProductBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        productRecyclerView.let {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = productAdapter
        }
        binding.productRecyclerView.adapter = productAdapter
        viewModel.products.observe(this
            , Observer { setData(it) })
        viewModel.exception.observe(this, Observer { errorHandler(it) })
        viewModel.loadProducts()
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = true
            viewModel.loadProducts()
        }

        sortProducts.setOnClickListener {
            val dialog = Dialog(ContextThemeWrapper(context, com.citycash.shop.R.style.DialogSlideAnim))
            dialog.let {
                it.window.setGravity(Gravity.BOTTOM)
                it.requestWindowFeature(Window.FEATURE_NO_TITLE)
                it.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                it.setContentView(com.citycash.shop.R.layout.layout_sort)
            }
            val arrayAdapter = ArrayAdapter<String>(context, R.layout.select_dialog_singlechoice)
            arrayAdapter.add("A")
            arrayAdapter.add("B")
            arrayAdapter.add("C")
            dialog.sortList.let {
                it.adapter = arrayAdapter
                it.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, l ->
                    val selectedSortItem = arrayAdapter.getItem(i)
                    Toast.makeText(activity, selectedSortItem, Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }
            dialog.show()
        }
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

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun setData(products: List<Product>) {
        binding.hasProducts = products.isNotEmpty()
        swipeRefreshLayout.isRefreshing = false
        productAdapter.products.value = products
    }
}

