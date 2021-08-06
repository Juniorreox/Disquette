package fr.juniorreox.disquette.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

typealias FragmentBuilder = () -> Fragment

class pagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragmentBuilders = mutableListOf<FragmentBuilder>()

    fun add(fragmentBuilder: FragmentBuilder) {
        fragmentBuilders.add(fragmentBuilder)
    }
    fun remove(position: Int){
        fragmentBuilders.removeAt(position)
    }

    /**
     * Dynamic replacement of fragments
     */
    fun set(position: Int, fragmentBuilder: FragmentBuilder) {
        fragmentBuilders[position] = fragmentBuilder
    }

    override fun getItemCount() = fragmentBuilders.size

    override fun createFragment(position: Int) = fragmentBuilders[position].invoke()

}