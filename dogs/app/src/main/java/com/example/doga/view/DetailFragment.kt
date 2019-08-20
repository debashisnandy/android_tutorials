package com.example.doga.view


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation

import com.example.doga.R
import com.example.doga.Util.getProgressDrawable
import com.example.doga.Util.loadImage
import com.example.doga.databinding.FragmentDetailBinding
import com.example.doga.viewmodel.DetailsViewModel
import kotlinx.android.synthetic.main.fragment_detail.*


class DetailFragment : Fragment() {

    private var dogUUID = 0
    private lateinit var detailsViewModel: DetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailsViewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)

        arguments?.let {
            dogUUID = DetailFragmentArgs.fromBundle(it).dogUuid
            detailsViewModel.fetch(dogUUID)
        }

        detailsViewModel.dogLiveData.observe(this, Observer {
            it?.let {dogs->
                FragmentDetailBinding.bind(view).dogDetails = dogs
            }
        })

    }
}
