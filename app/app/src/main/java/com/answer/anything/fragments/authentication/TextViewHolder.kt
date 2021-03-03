package com.answer.anything.fragments.authentication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.answer.anything.data.ResearchQuestion
import com.answer.anything.databinding.TextApresentationViewHolderBinding

class TextViewHolder(val text: String): Fragment() {
    private val TAG = "TextViewHolder"
    private lateinit var binding: TextApresentationViewHolderBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TextApresentationViewHolderBinding.inflate(inflater, container, false)
        Log.d(TAG, "Text => ${text}")
        binding.text = text
        return binding.root
    }
}