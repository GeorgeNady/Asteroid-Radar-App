package com.udacity.asteroidradar.ui.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.ui.main.adapter.AsteroidClickListener
import com.udacity.asteroidradar.ui.main.adapter.AsteroidsRecyclerViewAdapter
import com.udacity.asteroidradar.utile.RequestState

enum class AsteroidsView {
    ALL, TODAY, WEEK
}

class MainFragment : Fragment() {

    private val binding by lazy { FragmentMainBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<MainViewModel>()
    private val asteroidsView = MutableLiveData(AsteroidsView.ALL)

    private val adapter by lazy {
        val adapterClickListener by lazy { AsteroidClickListener { asteroid ->
                findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid)) } }
        AsteroidsRecyclerViewAdapter(adapterClickListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        with(binding) {
            lifecycleOwner = this@MainFragment
            bViewModel = viewModel
            bAdapter = adapter

            with(viewModel) {

                asteroidsView.observe(viewLifecycleOwner) {
                    it?.let { asteroidsView ->
                        when(asteroidsView) {
                            AsteroidsView.ALL -> asteroids.observe(viewLifecycleOwner, asteroidsObserver())
                            AsteroidsView.TODAY -> todayAsteroids.observe(viewLifecycleOwner, asteroidsObserver())
                            AsteroidsView.WEEK -> weekAsteroids.observe(viewLifecycleOwner, asteroidsObserver())
                        }
                    }
                }

                asteroids.observe(viewLifecycleOwner) {
                    adapter.submitList(it)
                }

                imageLoadingRequestState.observe(viewLifecycleOwner) {

                }

                asteroidsLoadingRequestState.observe(viewLifecycleOwner) {
                    when(it) {
                        RequestState.LOADING -> progress.visible()
                        else -> progress.hide()
                    }
                }
            }

        }

        return binding.root
    }

    private fun asteroidsObserver(): Observer<List<Asteroid>> {
        return Observer {
            adapter.submitList(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.next_week_asteroids -> {
                asteroidsView.value = AsteroidsView.WEEK
                true
            }
            R.id.today_asteroids -> {
                asteroidsView.value = AsteroidsView.TODAY
                true
            }
            R.id.saved_asteroids -> {
                asteroidsView.value = AsteroidsView.ALL
                true
            }
            else -> false
        }
    }

    private fun View.hide() {
        visibility = View.GONE
    }

    private fun View.visible() {
        visibility = View.VISIBLE
    }

}
