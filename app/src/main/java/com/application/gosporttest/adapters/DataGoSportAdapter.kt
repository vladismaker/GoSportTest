package com.application.gosporttest.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.application.gosporttest.R
import com.application.gosporttest.room.GoSport
import com.squareup.picasso.Picasso

class DataGoSportAdapter :
    ListAdapter<GoSport, DataGoSportAdapter.DataGoSportViewHolder>(DataGoSportDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataGoSportViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.card_two_my_app, parent, false)
        return DataGoSportViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataGoSportViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    inner class DataGoSportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: GoSport) {
            val tvName: TextView = itemView.findViewById(R.id.id_name)
            val tvDescription: TextView = itemView.findViewById(R.id.id_description)
            val ivImage: ImageView = itemView.findViewById(R.id.id_image)

            tvName.text = data.name
            tvDescription.text = data.description
            Picasso.get().load(data.image).into(ivImage)
        }
    }

    private class DataGoSportDiffCallback : DiffUtil.ItemCallback<GoSport>() {
        override fun areItemsTheSame(oldItem: GoSport, newItem: GoSport): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: GoSport, newItem: GoSport): Boolean {
            return oldItem == newItem
        }
    }
}