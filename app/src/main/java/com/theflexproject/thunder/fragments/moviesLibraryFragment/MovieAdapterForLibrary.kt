package com.theflexproject.thunder.fragments.moviesLibraryFragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.theflexproject.thunder.databinding.MediaItemBinding
import com.theflexproject.thunder.fragments.MovieDetailsFragment
import com.theflexproject.thunder.model.Movie

class MovieAdapterForLibrary : ListAdapter<Movie, MovieAdapterForLibrary.MovieViewHolder>(DiffUtilImp) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = MediaItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)


    }

    class MovieViewHolder(private val binding: MediaItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(movie.poster_path)
                    .into(posterInMediaItem)
                nameInMediaItem.text = movie.title
                yearInMediaItem.text = movie.release_date
            }
            binding.movieCard.setOnClickListener {
//                findNavController().navigate()
            }
        }
    }

    companion object DiffUtilImp : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }


}