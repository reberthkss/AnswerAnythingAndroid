package com.answer.anything.fragments.questions

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedDispatcher
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.answer.anything.R
import com.answer.anything.data.questions
import com.answer.anything.databinding.QuestionsFragmentBinding
import com.answer.anything.model.InProgressResearchViewModel

class QuestionsFragment : Fragment() {
    private lateinit var binding: QuestionsFragmentBinding
    private val inProgressResearchViewModel: InProgressResearchViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val pagerAdapter = ViewPagerAdapter(this, inProgressResearchViewModel.getResearch().value?.questions)
        binding = QuestionsFragmentBinding.inflate(inflater, container, false)
        binding.pager.adapter = pagerAdapter
        return binding.root
    }
}