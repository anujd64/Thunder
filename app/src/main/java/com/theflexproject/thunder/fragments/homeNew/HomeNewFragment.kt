package com.theflexproject.thunder.fragments.homeNew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.theflexproject.thunder.R
import com.theflexproject.thunder.database.DatabaseClient
import com.theflexproject.thunder.databinding.FragmentNewHomeBinding
import com.theflexproject.thunder.fragments.MovieDetailsFragment
import com.theflexproject.thunder.fragments.TvShowDetailsFragment
import com.theflexproject.thunder.model.MyMedia
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeNewFragment : Fragment() {
    private lateinit var recentlyAdded: List<MyMedia>
    private lateinit var topRated: List<MyMedia>
    private lateinit var recentlyReleased: List<MyMedia>
    private lateinit var watchlistMovies: List<MyMedia>
    private lateinit var watchlistShows: List<MyMedia>
    private lateinit var lastPlayed: List<MyMedia>
    private lateinit var recentlyReleasedTV: List<MyMedia>
    private lateinit var topRatedTV: List<MyMedia>

    private val viewModel by viewModels<HomeNewViewModel>()

    private lateinit var mAdapter: ParentAdapter

    private var retrieved: MutableLiveData<Boolean> = MutableLiveData(false)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewHomeBinding.inflate(inflater, container, false)
        val recyclerView = binding.rvParent

        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch(Dispatchers.IO) {
            recentlyAdded = DatabaseClient.getInstance(requireContext()).appDatabase.movieDao()
                .getrecentlyadded()
            recentlyReleased =
                DatabaseClient.getInstance(requireContext()).appDatabase.movieDao()
                    .getrecentreleases()
            topRated =
                DatabaseClient.getInstance(requireContext()).appDatabase.movieDao().topRated
            lastPlayed =
                DatabaseClient.getInstance(requireContext()).appDatabase.movieDao().played
            watchlistMovies =
                DatabaseClient.getInstance(requireContext()).appDatabase.movieDao().watchlisted
            watchlistShows =
                DatabaseClient.getInstance(requireContext()).appDatabase.tvShowDao().watchlisted
            topRatedTV =
                DatabaseClient.getInstance(requireContext()).appDatabase.tvShowDao().topRated
            recentlyReleasedTV =
                DatabaseClient.getInstance(requireContext()).appDatabase.tvShowDao().newShows

            retrieved.postValue(true)

        }

        retrieved.observe(viewLifecycleOwner) {
            if (it) {
                binding.progressBar.visibility = View.GONE
                binding.rvParent.visibility = View.VISIBLE
                recyclerView.layoutManager =
                    LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

                mAdapter = ParentAdapter { id, isTV ->
                    if (isTV) {
                        val action = HomeNewFragmentDirections.actionHomeNewFragmentToTvShowDetailsFragment(id)
                        findNavController().navigate(action)
//                        val tvShowDetailsFragment = TvShowDetailsFragment(id)
//                        requireActivity()
//                            .supportFragmentManager
//                            .beginTransaction()
//                            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
//                            .add(R.id.container, tvShowDetailsFragment).addToBackStack(null)
//                            .commit()
                    }else{
                        val action = HomeNewFragmentDirections.actionHomeNewFragmentToMovieDetailsFragment(id)
                        findNavController().navigate(action)
//                        val movieDetailsFragment = MovieDetailsFragment(id)
//                        requireActivity()
//                            .supportFragmentManager
//                            .beginTransaction()
//                            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
//                            .add(R.id.container, movieDetailsFragment).addToBackStack(null)
//                            .commit()
                    }
                }


                recyclerView.adapter = mAdapter
                mAdapter.setData(
                    listOf(
                        ParentModel("Recently Added", recentlyAdded),
                        ParentModel("Recently Released", recentlyReleased),
                        ParentModel("Top Rated", topRated),
                        ParentModel("Recently Released TV Shows", recentlyReleasedTV),
                        ParentModel("Top Rated TV Shows", topRatedTV),
                        ParentModel("Last Played", lastPlayed),
                        ParentModel("Watchlist Movies", watchlistMovies),
                        ParentModel("Watchlist TV Shows", watchlistShows)
                    )
                )
            }

        }


        return binding.root
    }

}