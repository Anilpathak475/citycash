package com.citycash.shop

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.databinding.BindingAdapter

val Context.networkStatus
    @SuppressLint("MissingPermission")
    get() :Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        return networkInfo != null && networkInfo.isConnected
    }

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

private fun noInternetDialog(activity: Activity) {
    try {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("No Internet Connection")
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit")
        builder.setPositiveButton("Ok") { dialog, _ -> dialog.dismiss() }
        builder.show()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

class FilterEvent(val filterQuery: String)

val NAV_ACTION_PARAM = "nav_action_id"

interface AutoUpdatableAdapter {

    fun <T> RecyclerView.Adapter<*>.autoNotify(old: List<T>, new: List<T>, compare: (T, T) -> Boolean) {
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return compare(old[oldItemPosition], new[newItemPosition])
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return old[oldItemPosition] == new[newItemPosition]
            }

            override fun getOldListSize() = old.size

            override fun getNewListSize() = new.size
        })

        diff.dispatchUpdatesTo(this)
    }
}






