package com.udacity.asteroidradar.main


import android.content.DialogInterface.OnClickListener
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.ListItemViewBinding

class AsteroidAdapter(val clickListener: AsteroidListener) : ListAdapter<Asteroid, AsteroidAdapter.AsteroidViewHolder>(DiffCallback) {
    class AsteroidViewHolder(private var binding:ListItemViewBinding):
        RecyclerView.ViewHolder(binding.root) {
            fun bind(asteroid: Asteroid,clickListener: AsteroidListener){
                binding.asteroid = asteroid
                binding.clickListener = clickListener
                binding.executePendingBindings()
            }

    }

    companion object DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        return AsteroidViewHolder(ListItemViewBinding.inflate((LayoutInflater.from(parent.context)), parent, false))
       //return AsteroidViewHolder(ListItemViewBinding.inflate((LayoutInflater.from(parent.context))))
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val asteroid = getItem(position)
        holder.bind(asteroid,clickListener)
    }
}

class AsteroidListener(val clickListener: (asteroid: Asteroid) ->Unit){
    fun onClick(asteroid: Asteroid) = clickListener(asteroid)
}