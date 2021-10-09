package com.lucifer.marvelapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.lucifer.marvelapplication.R
import com.lucifer.marvelapplication.databinding.RecyclerItemBinding
import com.lucifer.marvelapplication.models.character.Result

class CharacterAdapter(private val listener: CharacterItemClicked) : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    private val items: ArrayList<Result> = ArrayList()

    inner class CharacterViewHolder(val recyclerItemBinding: RecyclerItemBinding) : RecyclerView.ViewHolder(recyclerItemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CharacterViewHolder(
            DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),R.layout.recycler_item, parent, false
            )
    )

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val currentCharacter = items[position]
        holder.recyclerItemBinding.nameRecycler = currentCharacter.name
        holder.recyclerItemBinding.imageRecycler = currentCharacter.thumbnail.path+"/standard_fantastic."+currentCharacter.thumbnail.extension
        holder.recyclerItemBinding.root.setOnClickListener {
            listener.onItemClicked(currentCharacter)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateList(newList: List<Result>){
        items.clear()
        items.addAll(newList)

        notifyDataSetChanged()
    }
}

interface CharacterItemClicked {
    fun onItemClicked(character: Result)
}