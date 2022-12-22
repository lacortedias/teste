package com.example.teste.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.teste.databinding.MainAdapterBinding
import com.example.teste.framework.data.Repositories

class MainAdapter(private var onItemClicked: (Repositories) -> Unit): RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private var data: List<Repositories> = listOf()

    fun setList(data: List<Repositories>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MainAdapterBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val word = data[position]
        holder.bind(word, onItemClicked)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(private val binding: MainAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Repositories, onItemClicked: (Repositories) -> Unit) {
            binding.txtAdapter.text = data.word

            binding.root.setOnClickListener {
                onItemClicked(data)
            }
        }
    }


}