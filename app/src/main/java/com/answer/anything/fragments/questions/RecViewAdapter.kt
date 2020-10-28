package com.answer.anything.fragments.questions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.answer.anything.R
import com.answer.anything.databinding.QuestionsViewHolderBinding

class RecViewAdapter (private val options: HashMap<Int, String>, private val setSelectedOption: (option: Int) -> Unit) : RecyclerView.Adapter<RecViewAdapter.ViewHolder>() {
    class ViewHolder(val holder: View): RecyclerView.ViewHolder(holder) {
        lateinit var binding: QuestionsViewHolderBinding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context);
        val holder = QuestionsViewHolderBinding.inflate(inflater, parent, false)
        return ViewHolder(holder.root).apply {
            binding = holder
        }
    }

    override fun getItemCount(): Int = options.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.text = options[position]
        holder.binding.optionButton.setOnClickListener{
            setSelectedOption(position)
            holder.binding.optionButton.setStrokeColorResource(R.color.green)
        }
    }

}