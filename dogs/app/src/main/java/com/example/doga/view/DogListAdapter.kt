package com.example.doga.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.doga.R
import com.example.doga.Util.getProgressDrawable
import com.example.doga.Util.loadImage
import com.example.doga.databinding.ItemDogBinding
import com.example.doga.model.DogBreed
import kotlinx.android.synthetic.main.item_dog.view.*

class DogListAdapter(val dogList: ArrayList<DogBreed>):RecyclerView.Adapter<DogListAdapter.DogViewHolder>(), DogClickListener {

    fun updateDogList(newDogList: List<DogBreed>){
        dogList.clear()
        dogList.addAll(newDogList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder = DogViewHolder(DataBindingUtil.inflate<ItemDogBinding>(LayoutInflater
        .from(parent.context),R.layout.item_dog,parent,false))

    override fun getItemCount(): Int = dogList.size

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {3
        holder.view.dog = dogList[position]
        holder.view.listener = this
//        holder.view.name.text = dogList[position].dogBreed
//        holder.view.lifespan.text = dogList[position].lifeSpan
//        holder.view.setOnClickListener {
//            val action = ListFragmentDirections.actionDetailFragment()
//            action.dogUuid = dogList[position].uuid
//            Navigation.findNavController(it).navigate(action)
//        }
//        holder.view.imageView.loadImage(dogList[position].imageUrl, getProgressDrawable(holder.view.imageView.context))
    }

    override fun onDogClicked(v: View) {
        val action = ListFragmentDirections.actionDetailFragment()
            action.dogUuid = v.dogId.text.toString().toInt()
            Navigation.findNavController(v).navigate(action)
    }

    class DogViewHolder(var view:ItemDogBinding):RecyclerView.ViewHolder(view.root)
}