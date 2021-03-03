package com.answer.anything.fragments.questions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.answer.anything.databinding.QuestionsFragmentBinding
import com.answer.anything.model.AnswerViewModel

class QuestionsFragment : Fragment() {
    private lateinit var binding: QuestionsFragmentBinding
    private val answerViewModel: AnswerViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val pagerAdapter = QuestionsViewPager(this, answerViewModel.getAnsweringResearch().value?.questions)
        binding = QuestionsFragmentBinding.inflate(inflater, container, false)
        binding.pager.adapter = pagerAdapter
        return binding.root
    }
}