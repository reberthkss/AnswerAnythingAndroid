package com.answer.anything.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.answer.anything.data.Research
import com.answer.anything.data.ResearchStatus
import com.answer.anything.repository.ResearchRepository
import com.facebook.Profile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class ResearchViewModel : ViewModel() {
    private val TAG = "ResearchViewModel"
    private val researchLiveData: MutableLiveData<List<Research>> = MutableLiveData(null)
    private val allResearchs: MutableLiveData<List<Research>> = MutableLiveData()
    private val isLoading: MutableLiveData<Boolean> = MutableLiveData(true);
    private val researchRepository = ResearchRepository()
    private val authenticatedUser = FirebaseAuth.getInstance().currentUser
    private lateinit var unsubscribeFirestore: ListenerRegistration

    /*Called once*/
    fun config() {
        Log.d(TAG, "User => ${authenticatedUser}")
        unsubscribeFirestore = researchRepository.setSnapShotListener {
            allResearchs.value = it
            filterOpenResearchs()
        }
        isLoading.value = false

    }

    fun getResearchs(): LiveData<List<Research>> {
        return researchLiveData;
    }

    fun filterOpenResearchs() {
        val openResearchs = allResearchs.value?.filter { it.status == ResearchStatus.OPEN };
        researchLiveData.value = allResearchs.value?.filter { it.status == ResearchStatus.OPEN };
    }

    fun filterClosedResearchs() {
        researchLiveData.value = allResearchs.value?.filter { it.status == ResearchStatus.CLOSED };
    }

    fun getLoadingStatus(): LiveData<Boolean> {
        return isLoading
    }

    fun unsubscribe() {
        unsubscribeFirestore.remove()
    }

}