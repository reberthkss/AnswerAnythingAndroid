package com.answer.anything.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.answer.anything.data.*
import com.answer.anything.repository.AnswerResearch
import com.answer.anything.repository.EndAnswerResearchPayload
import com.answer.anything.repository.ResearchRepository
import com.answer.anything.repository.SaveAnsweredQuestionPayload
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

enum class AnswerViewModelStatus(val value: String) {GETTING_USER_DATA("getting_user_data"), ANSWERING("answering"), DONE("done")}

/* That class going:
*  * Set answered questions
*  * Control status of answer
*  * Supply answering research data
* */

class AnswerViewModel: ViewModel() {
    private val TAG = "AnswerViewModel"
    private val id: MutableLiveData<String?> = MutableLiveData<String?>(null)
    private val status: MutableLiveData<AnswerViewModelStatus> = MutableLiveData(AnswerViewModelStatus.GETTING_USER_DATA)
    private val answerRepository = AnswerResearch()
    private val researchRepository = ResearchRepository()
    private val research: MutableLiveData<Research?> = MutableLiveData(null)
    private val answerResearchData: MutableLiveData<AnswerData?> = MutableLiveData(null)
    private val loading = MutableLiveData(false)


    fun startQuestionnaire(researchId: String, userData: AnswerUserData) {
        loading.value = true
        viewModelScope.launch {
            val answerData = AnswerData(userData)
            id.value = answerRepository.startQuestionnaire(researchId, answerData)
            if (id.value != null) {
                answerResearchData.value = answerData
                research.value = researchRepository.findById(researchId)
                if (research.value != null) {
                    status.value = AnswerViewModelStatus.ANSWERING
                }
            }
            loading.value = false
        }

    }

    fun registerSelectedOption(questionId: String, selectedOption: Int, prevSelectedOption: Int?) {
        loading.value = true
        viewModelScope.launch {
            /*TODO - Check if that question is already selected (if already is in array)*/
            answerResearchData.value?.answeredQuestions?.add(AnsweredQuestion(questionId, selectedOption, prevSelectedOption))
        }
        loading.value = false
    }

    fun endQuestionnaire() {
        viewModelScope.launch {
            loading.value = true
            answerResearchData
                .value
                ?.answeredQuestions
                ?.forEach {
                    answerRepository.saveAnsweredQuestion(object: SaveAnsweredQuestionPayload {
                        override val researchId: String?
                            get() = research.value?.id
                        override val answerResearchId: String?
                            get() = id.value
                        override val answeredQuestionId: String?
                            get() = it.questionId
                        override val selectedOption: Int
                            get() = it.selectedOption
                        override val prevSelectedOption: Int?
                            get() = it.prevSelectedOption
                    })
                }
            
            val res = answerRepository.endQuestionnaire(object: EndAnswerResearchPayload {
                override val researchId: String?
                    get() = research.value?.id
                override val answeredResearchId: String?
                    get() = id.value

            })

            if (res) {
                /* Successfull ended questionnaire */
            } else {
                /* Error on end questionnaire */
            }
            loading.value = false
        }
    }

    fun getAnsweringResearch(): LiveData<Research?> {
        return research
    }

    fun getStatusOfAnswer(): LiveData<AnswerViewModelStatus> {
        return status
    }

    fun getLoadingStatus(): LiveData<Boolean> {
        return loading
    }

    fun setStatus(newStatus: AnswerViewModelStatus) {
        status.value = newStatus
    }







}