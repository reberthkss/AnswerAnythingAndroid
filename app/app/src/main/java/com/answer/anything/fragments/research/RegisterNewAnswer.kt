package com.answer.anything.fragments.research

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.answer.anything.databinding.RegisterNewAnswerBinding

class RegisterNewAnswer : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = RegisterNewAnswerBinding.inflate(inflater, container, false)
        return binding.root
    }
}