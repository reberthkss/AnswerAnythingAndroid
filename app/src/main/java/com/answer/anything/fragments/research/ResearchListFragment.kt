package com.answer.anything.fragments.research

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.answer.anything.R
import com.answer.anything.data.Research
import com.answer.anything.databinding.ResearchListFragmentBinding
import com.answer.anything.model.InProgressResearchViewModel
import com.answer.anything.model.ResearchViewModel

class ResearchListFragment : Fragment() {
    private lateinit var binding: ResearchListFragmentBinding
    private val inProgressResearchViewModel: InProgressResearchViewModel by activityViewModels()
    private val researchViewModel: ResearchViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        researchViewModel
            .getResearchs()
            .observe(viewLifecycleOwner, Observer {
                if (it != null && it.isEmpty()) {
                    binding.recView.researchRecView.visibility = GONE
                    binding.loadingIndicatorContainer.visibility = GONE
                    binding.noneResearchFoundContainer.visibility = VISIBLE
                } else {
                    setAdapterToRecView(it)
                }
            }
        )

        researchViewModel
            .getLoadingStatus()
            .observe(viewLifecycleOwner, Observer {
                if (it) {
                    binding.loadingIndicatorContainer.visibility = VISIBLE
                    binding.recView.researchRecView.visibility = GONE
                    binding.noneResearchFoundContainer.visibility = GONE
                } else {
                    binding.loadingIndicator.visibility = GONE;
                    binding.recView.researchRecView.visibility = VISIBLE
                }
            })
        divider.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.line_divider)!!)
        binding = ResearchListFragmentBinding.inflate(inflater, container, false)
//        binding.recView.researchRecView.addItemDecoration(divider)
        return binding.root;
    }

    private fun setAdapterToRecView(it: List<Research>?) {
        binding.recView.researchRecView.adapter = ResearchAdapter(it ?: listOf()) {
            inProgressResearchViewModel.setResearch(it)
        }
    }


}