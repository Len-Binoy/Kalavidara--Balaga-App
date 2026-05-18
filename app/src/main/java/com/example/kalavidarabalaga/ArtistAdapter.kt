package com.example.kalavidarabalaga

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kalavidarabalaga.databinding.ItemArtistBinding

class ArtistAdapter(
    private var list: List<Artist>,
    private val onClick: (Artist) -> Unit
) : RecyclerView.Adapter<ArtistAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemArtistBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemArtistBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val artist = list[position]
        val context = holder.itemView.context

        holder.binding.tvName.text = artist.name
        
        // Use string resources with placeholders for better localization
        holder.binding.tvLocation.text = context.getString(R.string.format_location, artist.location)
        holder.binding.tvDistrict.text = context.getString(R.string.format_district, artist.district)
        holder.binding.tvArtType.text = context.getString(R.string.format_art_type, artist.artType)

        Glide.with(context)
            .load(artist.imageUrl)
            .placeholder(android.R.drawable.progress_horizontal)
            .error(android.R.drawable.ic_menu_report_image)
            .into(holder.binding.ivArtist)

        holder.itemView.setOnClickListener {
            onClick(artist)
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateList(newList: List<Artist>) {
        list = newList
        notifyDataSetChanged()
    }
}
