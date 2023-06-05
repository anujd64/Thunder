package com.theflexproject.thunder.fragments.homeNew

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.theflexproject.thunder.Constants.TMDB_IMAGE_BASE_URL
import com.theflexproject.thunder.MainActivity
import com.theflexproject.thunder.R
import com.theflexproject.thunder.adapter.BannerRecyclerAdapter
import com.theflexproject.thunder.databinding.MediaItemBinding
import com.theflexproject.thunder.databinding.MovieItemBannerBinding
import com.theflexproject.thunder.fragments.MovieDetailsFragment
import com.theflexproject.thunder.fragments.TvShowDetailsFragment
import com.theflexproject.thunder.model.Movie
import com.theflexproject.thunder.model.MyMedia
import com.theflexproject.thunder.model.TVShowInfo.TVShow

class ChildAdapter(private val shouldBeBanner: Boolean, private val onItemClick: (Int,Boolean) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val children: MutableList<MyMedia> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_BANNER) {
            val binding = MovieItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            BannerViewHolder(binding)
        } else {
            val binding = MediaItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            PosterViewHolder(binding)
        }
    }

    fun setData (data : List<MyMedia>){
        children.clear()
        children.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return children.size
    }

    override fun onBindViewHolder(holder:  RecyclerView.ViewHolder, position: Int) {
        val child = children[position]

        if (holder is PosterViewHolder && child is Movie) {
            holder.bind(child)
        } else if (holder is PosterViewHolder && child is TVShow) {
            holder.bind(child)
        }else if (holder is BannerViewHolder && child is Movie) {
            holder.bind(child)
        } else if (holder is BannerViewHolder && child is TVShow) {
            holder.bind(child)
        }
//        if (child is Movie)
//            holder.bind(child as Movie)
//        else
//            holder.bind(child as TVShow)
    }


    override fun getItemViewType(position: Int): Int {
        return if (shouldBeBanner) {
            VIEW_TYPE_BANNER
        } else {
            VIEW_TYPE_POSTER
        }
    }


    inner class PosterViewHolder(private val binding: MediaItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(child: Movie) {
            binding.nameInMediaItem.text = (child).title
            Glide.with(binding.root.context)
                .load(TMDB_IMAGE_BASE_URL + child.poster_path)
                .into(binding.posterInMediaItem)

            binding.yearInMediaItem.text = child.release_date
            binding.root.setOnClickListener {
                onItemClick(child.id,false)
            }
        }

        fun bind(child: TVShow) {
            binding.nameInMediaItem.text = (child as TVShow).name
            Glide.with(binding.root.context)
                .load(TMDB_IMAGE_BASE_URL + child.poster_path)
                .into(binding.posterInMediaItem)
            binding.root.setOnClickListener {
                onItemClick(child.id,true)
            }
        }

    }


    inner class BannerViewHolder(private val binding: MovieItemBannerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(child: Movie) {
            binding.textView4.text = (child).title
            Glide.with(binding.root.context)
                .load(TMDB_IMAGE_BASE_URL + child.backdrop_path)
                .into(binding.moviePoster)
            binding.root.setOnClickListener {
                onItemClick(child.id,false)
            }
        }

        fun bind(child: TVShow) {
            binding.textView4.text = (child).name
            Glide.with(binding.root.context)
                .load(TMDB_IMAGE_BASE_URL + child.backdrop_path)
                .into(binding.moviePoster)
            binding.root.setOnClickListener {
                onItemClick(child.id,true)
            }
        }

    }
    companion object {
        private const val VIEW_TYPE_POSTER = 1
        private const val VIEW_TYPE_BANNER = 2
    }
}