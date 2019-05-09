package com.citycash.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.citycash.shop.databinding.FragmentNavigationConfigBinding

class NavigationConfigFragment : Fragment() {
    private var navId: Int = 0
    lateinit var binding: FragmentNavigationConfigBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            navId = it.getInt("navId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNavigationConfigBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var navTagValue = ""
        if (navId > 0)
            when (navId) {
                R.id.nav_account -> navTagValue = "My Account"
                R.id.nav_orders -> navTagValue = "My Orders"
                R.id.nav_offers -> navTagValue = "Offers"

            }
        binding.navTag = navTagValue
    }

}
