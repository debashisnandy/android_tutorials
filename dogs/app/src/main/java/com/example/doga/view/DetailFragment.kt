package com.example.doga.view


import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.telephony.SmsManager
import android.view.*
import androidx.appcompat.app.AlertDialog
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
import com.example.doga.databinding.SendSmsDialogBinding
import com.example.doga.model.DogBreed
import com.example.doga.model.DogPalette
import com.example.doga.model.SmsInfo
import com.example.doga.viewmodel.DetailsViewModel


class DetailFragment : Fragment() {

    private var dogUUID = 0
    private lateinit var detailsViewModel: DetailsViewModel
    private lateinit var fragmentDataBind:FragmentDetailBinding
    private var sendSmsStarted = false
    private var currentDog:DogBreed? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
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
            currentDog = it
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.details_menu,menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId){
            R.id.action_send_sms->{
                sendSmsStarted = true
                (activity as MainActivity).checkSmsPermission()
            }
            R.id.action_share->{

            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun onPermissionResult(permissionGranted:Boolean){

        if (sendSmsStarted && permissionGranted){
            context?.let {
                val smsInfo = SmsInfo("","${currentDog?.dogBreed} breed for ${currentDog?.breedFor}", currentDog?.imageUrl)
                val dialogBinding= DataBindingUtil.inflate<SendSmsDialogBinding>(LayoutInflater.from(it),R.layout.send_sms_dialog,null,false)
                AlertDialog.Builder(it)
                    .setView(dialogBinding.root)
                    .setPositiveButton("Send SMS"){dialog, which ->
                        if (!dialogBinding.smsDestination.text.isNullOrEmpty()){
                            smsInfo.to = dialogBinding.smsDestination.text.toString()
                            sendSms(smsInfo)
                        }
                    }.setNegativeButton("Cancel"){dialog, which ->

                    }.show()
                dialogBinding.smsInfo = smsInfo
            }
        }
    }

    private fun sendSms(smsInfo: SmsInfo) {
        val intent = Intent(context,MainActivity::class.java)
        val pi = PendingIntent.getActivity(context,0,intent,0)
        val sms = SmsManager.getDefault()
        sms.sendTextMessage(smsInfo.to,null,smsInfo.text,pi,null)
    }
}
