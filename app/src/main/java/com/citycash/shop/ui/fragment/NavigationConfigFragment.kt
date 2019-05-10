package com.citycash.shop.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.citycash.shop.R
import com.citycash.shop.databinding.FragmentNavigationConfigBinding

class NavigationConfigFragment : Fragment() {
    lateinit var binding: FragmentNavigationConfigBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNavigationConfigBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            if (it.containsKey("navId")) {
                val navId = it.getInt("navId")
                binding.navTag = when (navId) {
                    R.id.nav_account -> "My Account"
                    R.id.nav_orders -> "My Orders"
                    R.id.nav_offers -> "Offers"
                    else -> ""
                }
            }
        }

    }
}
