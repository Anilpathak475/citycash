package com.citycash.shop.ui.activity

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.citycash.shop.FilterEvent
import com.citycash.shop.FilterFragment
import com.citycash.shop.R
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_dashboard_activity.*
import org.greenrobot.eventbus.EventBus


class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController
    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_activity)
        setupNavigation()
    }

    private fun setupNavigation() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        drawerLayout = findViewById(R.id.drawer_layout)

        navController = Navigation.findNavController(this, R.id.nav_hot_main_fragment)

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

        NavigationUI.setupWithNavController(navigation_view, navController)

        navigation_view.setNavigationItemSelectedListener(this)

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_hot_main_fragment), drawerLayout)
    }

    override fun onBackPressed() {
        if (!searchView!!.isIconified) {
            searchView!!.isIconified = true
            return
        }
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.dashboard_activity, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.action_search)
            .actionView as SearchView
        searchView!!.setSearchableInfo(searchManager
            .getSearchableInfo(componentName))
        searchView!!.maxWidth = Integer.MAX_VALUE

        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                EventBus.getDefault().post(
                    FilterEvent(
                        query
                    )
                )
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                EventBus.getDefault().post(
                    FilterEvent(
                       query
                    )
                )
                return true
            }

        })

        searchView!!.setOnCloseListener {
            EventBus.getDefault().post(
                FilterEvent(
                    ""
                )
            )
            true
        }
        return true
    }




    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        menuItem.isChecked = true
        drawerLayout.closeDrawers()

        val id = menuItem.itemId
        val bundel = Bundle()
        bundel.putInt("navId", id)
        navController.navigate(R.id.workInProgressFragment, bundel)
        return true

    }
}
