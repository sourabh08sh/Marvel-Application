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

class ComicsAdapter(private val listener: ComicItemClicked) : RecyclerView.Adapter<ComicsAdapter.ComicsViewHolder>() {

    private val items: ArrayList<com.lucifer.marvelapplication.models.comic.Result> = ArrayList()

    inner class ComicsViewHolder(val recyclerItemBinding: RecyclerItemBinding) : RecyclerView.ViewHolder(recyclerItemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ComicsViewHolder (
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),R.layout.recycler_item, parent, false
        )
    )

    override fun onBindViewHolder(holder: ComicsViewHolder, position: Int) {
        val currentComic = items[position]
        holder.recyclerItemBinding.nameRecycler = currentComic.title
        holder.recyclerItemBinding.imageRecycler = currentComic.thumbnail.path+"/standard_fantastic."+currentComic.thumbnail.extension
        holder.recyclerItemBinding.root.setOnClickListener {
            listener.onItemClicked(currentComic)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateList(newList: List<com.lucifer.marvelapplication.models.comic.Result>){
        items.clear()
        items.addAll(newList)

        notifyDataSetChanged()
    }
}

interface ComicItemClicked {
    fun onItemClicked(comic: com.lucifer.marvelapplication.models.comic.Result)
}