package com.digitalhouse.desafiofirebase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ItemAdapter (val listaGames: ArrayList<Game>, val listener: OnItemClickListener) :
RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val ivThumbnail: ImageView = itemView.findViewById(R.id.ivThumb)
        val tvTitle: TextView = itemView.findViewById(R.id.tvGameTitle)
        val tvYear: TextView = itemView.findViewById(R.id.tvGameYear)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition

            if (RecyclerView.NO_POSITION != position) {
                listener.onClick(position)
            }
        }
    }

    interface OnItemClickListener {
        fun onClick(position: Int)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val game = listaGames[position]
        holder.tvTitle.text = game.name
        holder.tvYear.text = game.year.toString()
        Picasso.get().load(game.img).fit().centerCrop().into(holder.ivThumbnail)
    }

    override fun getItemCount() = listaGames.size
}