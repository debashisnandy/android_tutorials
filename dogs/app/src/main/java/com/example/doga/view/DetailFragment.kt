package com.example.doga.view


import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.doga.R
import com.example.doga.databinding.FragmentDetailBinding
import com.example.doga.model.DogPalette
import com.example.doga.viewmodel.DetailsViewModel


class DetailFragment : Fragment() {

    private var dogUUID = 0
    private lateinit var detailsViewModel: DetailsViewModel
    private lateinit var fragmentDataBind:FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentDataBind = DataBindingUtil.inflate(inflater,R.layout.fragment_detail,container,false)
        return fragmentDataBind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailsViewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)

        arguments?.let {
            dogUUID = DetailFragmentArgs.fromBundle(it).dogUuid
            detailsViewModel.fetch(dogUUID)
        }

        observeModel()
    }

    private fun observeModel(){
        detailsViewModel.dogLiveData.observe(this, Observer {
            it?.let {dogs->
                fragmentDataBind.dogDetails = dogs
                dogs.imageUrl?.let {url->
                    setBackgroundColor(url)
                }
            }
        })
    }

    private fun setBackgroundColor(url:String){
        Glide.with(this)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>(){
                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Palette.from(resource)
                        .generate { palette->
                            val intColor = palette?.lightMutedSwatch?.rgb ?:0
                            val myPalette = DogPalette(intColor)
                            fragmentDataBind.palette = myPalette
                        }
                }
            })
    }
}
