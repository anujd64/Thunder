package com.theflexproject.thunder.fragments.moviesLibraryFragment

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.theflexproject.thunder.databinding.FragmentMoviesLibraryBinding

class MoviesLibraryFragment : Fragment() {

    private var _binding : FragmentMoviesLibraryBinding? = null
    private val binding get() = _binding

    private val viewModel by viewModels<MovieLibraryViewModel>()

    private lateinit var adapter: MovieAdapterForLibrary

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesLibraryBinding.inflate(inflater,container,false)

        initUI()

        subScribeToObservers()

        return binding!!.root
    }

    private fun subScribeToObservers() {
        viewModel.moviesDB.observe(viewLifecycleOwner) {
                adapter.submitList(it)
        }
    }

    private fun initUI() {
        val displayMetrics: DisplayMetrics = requireContext().resources.displayMetrics
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density
        val noOfItems = (dpWidth / 120).toInt()

        adapter = MovieAdapterForLibrary()

        binding!!.apply {
            recyclerLibraryMovies.layoutManager = GridLayoutManager(requireContext(), noOfItems)
            recyclerLibraryMovies.adapter = adapter
        }
    }


}