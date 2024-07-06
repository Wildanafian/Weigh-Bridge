package com.wildan.weighbridge.core.ui.helper

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController

/*
 * Created by Wildan Nafian on 7/4/24.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */

fun Fragment.gooTo(directions: NavDirections) = findNavController().navigate(directions)

fun Fragment.goBack() = NavHostFragment.findNavController(this).popBackStack()

fun Toolbar.setToolbarBackNavigationAction(fragment: Fragment) {
    this.setNavigationOnClickListener {
        NavHostFragment.findNavController(fragment).popBackStack()
    }
}