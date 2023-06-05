package com.theflexproject.thunder.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.theflexproject.thunder.databinding.BottomSheetChangeTmdbBinding
import com.theflexproject.thunder.model.Movie
import com.theflexproject.thunder.model.MyMedia
import com.theflexproject.thunder.model.TVShowInfo.Episode
import com.theflexproject.thunder.model.TVShowInfo.TVShow
import com.theflexproject.thunder.utils.SendGetRequestTMDB.tmdbGetByID
import com.theflexproject.thunder.utils.StringUtils.tmdbIdExtractor_FromLink
import com.theflexproject.thunder.utils.StringUtils.tmdbIdExtractor_FromLink_TV

class ChangeTMDBBottomSheet(private val myMedia: MyMedia) : BottomSheetDialogFragment() {

    private var _binding : BottomSheetChangeTmdbBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetChangeTmdbBinding.inflate(inflater,container,false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.changeTMDBIdButton.setOnClickListener {
            val thread = Thread {
                val link = binding.tmdbLinkText.text.toString()
                if ( !link.isNullOrEmpty() ) {
                    if (myMedia is Movie) {
                        //send get request tmdb movie
                        val id = tmdbIdExtractor_FromLink(link)

                        if (id != 0L) {
                            tmdbGetByID(myMedia, id, false)
                        } else {
                            var failed = false
                            try {
                                val longId = link.toLong()
                                tmdbGetByID(myMedia, longId, false)
                            } catch (e: Exception) {
                                failed = true
                                println("Failed to parse long $e")
                                requireActivity().runOnUiThread {
                                    binding.tmdbLinkText.error = "Invalid Link"
                                }
                            }
                        }
                    } else if (myMedia is TVShow || myMedia is Episode) {
                        //send get request tmdb tvshow
                        val id = tmdbIdExtractor_FromLink_TV(link)
                        if (id != 0L) {
                            tmdbGetByID(myMedia, id, true)
                        } else if (link.length < 15) {
                            try {
                                val longId = link.toLong()
                                tmdbGetByID(myMedia, longId, true)
                            } catch (e: Exception) {
                                println("Failed to parse long $e")
                            }
                        } else {
                            binding.invalidTMDBLink.visibility = View.VISIBLE
                        }
                    }
                }
            }
            thread.start()
            Toast.makeText(requireContext(), "Changed", Toast.LENGTH_LONG).show()
        }
    }
}
