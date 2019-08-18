package com.example.doga.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.doga.R
import com.example.doga.Util.getProgressDrawable
import com.example.doga.Util.loadImage
import com.example.doga.model.DogBreed
import kotlinx.android.synthetic.main.item_dog.view.*

class DogListAdapter(val dogList: ArrayList<DogBreed>):RecyclerView.Adapter<DogListAdapter.DogViewHolder>() {

    fun updateDogList(newDogList: List<DogBreed>){
        dogList.clear()
        dogList.addAll(newDogList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder = DogViewHolder(LayoutInflater
        .from(parent.context)
        .inflate(R.layout.item_dog,parent,false))

    override fun getItemCount(): Int = dogList.size

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        holder.view.name.text = dogList[position].dogBreed
        holder.view.lifespan.text = dogList[position].lifeSpan
        holder.view.setOnClickListener {
            val action = ListFragmentDirections.actionDetailFragment()
            action.dogUuid = position
            Navigation.findNavController(it).navigate(action)
        }
        holder.view.imageView.loadImage(dogList[position].imageUrl, getProgressDrawable(holder.view.imageView.context))
    }

    class DogViewHolder(var view:View):RecyclerView.ViewHolder(view)
}