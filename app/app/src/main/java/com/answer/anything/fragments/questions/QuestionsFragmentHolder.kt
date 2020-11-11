package com.answer.anything.fragments.questions

import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.answer.anything.R
import com.answer.anything.data.ResearchQuestion
import com.answer.anything.databinding.QuestionsFragmentHolderBinding
import com.answer.anything.model.AnswerViewModel
import com.answer.anything.model.AnswerViewModelStatus
import kotlinx.android.synthetic.main.questions_view_holder.view.*

class QuestionsFragmentHolder(val question: ResearchQuestion?, val isTheLastQuestion: Boolean): Fragment() {
    private lateinit var binding: QuestionsFragmentHolderBinding
    private val answerResearchViewModel: AnswerViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = QuestionsFragmentHolderBinding.inflate(inflater, container, false)
        /* FIX - Control  to research selected is a check for question is null. That's not correct*/
        if (question == null) {
            /* SHOW LAYOUT NONE RESEARCH WAS SELECTED */
        } else {
            val optionsAdapter = QuestionOptionsAdapter(question.options) {
                setSelectedOption(question, it)
            }
            binding.questionTitle = question.question
            binding.recView.questionsRecView.adapter = optionsAdapter
        }
        return binding.root
    }

    private fun setSelectedOption(question: ResearchQuestion, option: Int) {
        answerResearchViewModel.getAnsweringResearch().value?.questions?.first {
            it.id == question.id
        }
            ?.apply {
                if (selectedOption != null) {
                    binding.recView.questionsRecView.getChildAt(selectedOption!!).optionButton.setStrokeColorResource(R.color.grey)
                }
                selectedOption = option
                answerResearchViewModel.registerSelectedOption(id, option)
                if (isTheLastQuestion) {
                    binding.questionsContent.visibility = GONE
                    binding.finishContainer.visibility = VISIBLE

                    (binding.animatedDoneDrawable).apply {
                        setBackgroundResource(R.drawable.animated_done_vector_drawable)
                        (background as AnimatedVectorDrawable).apply {
                            start()
                            registerAnimationCallback(object: Animatable2.AnimationCallback (){
                                override fun onAnimationEnd(drawable: Drawable?) {
                                    answerResearchViewModel.endQuestionnaire()
                                    answerResearchViewModel.setStatus(AnswerViewModelStatus.DONE)
                                }
                            })
                        }
                    }
                }
            }
    }
}