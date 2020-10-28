package com.answer.anything.fragments.questions

import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.answer.anything.R
import com.answer.anything.data.ResearchQuestion
import com.answer.anything.data.ResearchStatus
import com.answer.anything.databinding.QuestionsFragmentHolderBinding
import com.answer.anything.model.InProgressResearchViewModel
import kotlinx.android.synthetic.main.questions_view_holder.view.*

class QuestionsFragmentHolder(val question: ResearchQuestion?, val isTheLastQuestion: Boolean): Fragment() {
    private lateinit var binding: QuestionsFragmentHolderBinding
    private val inProgressResearchViewModel: InProgressResearchViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = QuestionsFragmentHolderBinding.inflate(inflater, container, false)
        if (question == null) {
            /* SHOW LAYOUT NONE RESEARCH WAS SELECTED */
        } else {
            val recViewAdapter = RecViewAdapter(question.options) {
                setSelectedOption(question, it)
            }
            binding.currentQuestion = inProgressResearchViewModel.getResearch().value?.questions?.get(0)?.question
            binding.recView.questionsRecView.adapter = recViewAdapter
        }
        return binding.root
    }

    private fun setSelectedOption(question: ResearchQuestion, it: Int) {
        inProgressResearchViewModel.getResearch().value?.questions?.first {
            it.id == question.id
        }
            ?.apply {
                if (selectedOption != null) {
                    binding.recView.questionsRecView.getChildAt(selectedOption!!).optionButton.setStrokeColorResource(R.color.grey)
                }
                selectedOption = it
                Log.d("QuestionFragmentHolder", "ID => ${selectedOption}")
                if (isTheLastQuestion) {
                    binding.questionsContent.visibility = GONE
                    binding.finishContainer.visibility = VISIBLE

                    (binding.animatedDoneDrawable).apply {
                        setBackgroundResource(R.drawable.animated_done_vector_drawable)
                        (background as AnimatedVectorDrawable).apply {
                            start()
                            registerAnimationCallback(object: Animatable2.AnimationCallback (){
                                override fun onAnimationEnd(drawable: Drawable?) {
                                    inProgressResearchViewModel.finishResearch();
                                }
                            })
                        }
                    }
                }
            }
    }
}