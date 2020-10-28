package com.answer.anything.fragments.research

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.answer.anything.data.Research
import com.answer.anything.databinding.ResearchListViewHolderBinding

class ResearchAdapter (private val researchs: List<Research>, val onSelectResearch: (id: String) -> Unit): RecyclerView.Adapter<ResearchAdapter.ViewHolder>() {
    class ViewHolder (val holder: View): RecyclerView.ViewHolder(holder) {
        lateinit var binding: ResearchListViewHolderBinding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val holder = ResearchListViewHolderBinding.inflate(inflater, parent, false)
        return ViewHolder(holder.root).apply {
            this.binding = holder
        }
    }

    override fun getItemCount(): Int = researchs.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.title = researchs[position].title
        holder.binding.subtitle = researchs[position].subtitle
        holder.binding.description = researchs[position].description
        holder.binding.researchViewHolder.setOnClickListener {
            onSelectResearch(researchs[position].id)
        }
    }

}