package com.answer.anything.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.answer.anything.data.Research
import com.answer.anything.data.ResearchStatus
import com.answer.anything.data.researchsDataSet
import com.answer.anything.repository.ResearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InProgressResearchViewModel : ViewModel() {
    private val researchId: MutableLiveData<String?> = MutableLiveData(null)
    private val research: MutableLiveData<Research?> = MutableLiveData(null);
    private val researchRepository = ResearchRepository()

    fun getResearch(): LiveData<Research?> {
        return research
    }

    fun setResearch(id: String) {
        researchId.value = id
        viewModelScope.launch {
            val targetResearch = researchRepository.findById(id)
            research.value = targetResearch
        }
        /*TODO LOAD RESEARCH BY ID*/
    }

    fun finishResearch() {
        /* Check if all questions are answered*/
        viewModelScope.launch {
            val researchUpdated = researchRepository.updateStatus(researchId.value, ResearchStatus.CLOSED.name)
            research.value = researchUpdated
        }
    }
}