package com.answer.anything.repository

import android.util.Log
import com.answer.anything.data.Research
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

fun printFullData(response: QuerySnapshot, TAG: String) {
    response.documents.forEach {
        Log.d(TAG, "id => ${it.id}");
        Log.d(TAG, "title => ${it.getString("title")}")
        Log.d(TAG, "subtitle => ${it.getString("subtitle")}")
        Log.d(TAG, "description => ${it.getString("description")}")
        (it.get("options") as List<HashMap<*,*>>).forEach {
            Log.d(TAG, "Question => ${it["question"]}")
            Log.d(TAG, "Id question => ${it["id"]}")
            Log.d(TAG, "Selected option => ${it["selectedOption"]}")
            (it["options"] as HashMap<String, String>).forEach {
                Log.d(TAG, " key => ${it.key}, value => ${it.value}")
            }
        }
    }
}

class ResearchRepository() {
    companion object {
        private val TAG = "ResearchRepository"
    }
    private val firestore = FirebaseFirestore.getInstance()
    private val researchCollection = firestore.collection("researchs")


    suspend fun read(uid: String) = withContext(Dispatchers.IO) {
        val response: QuerySnapshot = researchCollection.get().await();
        response.documents.map {
            Research.from(it)
        }
    }

    fun update(uid: String, documentId: String, newResearchData: Research) {
        /*TODO*/
    }

    fun delete(uid: String, documentId: String) {
        /*TODO*/
    }

    fun save(researchData: Research) {
        val researchQuestionsMap = researchData.questions.map {
            hashMapOf(
                "id" to it.id,
                "question" to it.question,
                "options" to it.options.map {
                    it.key to it.value
                },
                "selectedOption" to it.selectedOption
            )
         }



        Log.d(TAG, "Hash map research questions map => ${researchQuestionsMap}");

        val researchMap = hashMapOf(
            "id" to researchData.id,
            "title" to researchData.title,
            "subtitle" to researchData.subtitle,
            "description" to researchData.description,
            "questions" to researchQuestionsMap
        )

        Log.d(TAG, "Hash map research map => ${researchMap}")
    }

    fun setSnapShotListener(onSuccess: (researchs: List<Research>?) -> Unit): ListenerRegistration {
        return researchCollection
            .addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                } else {
                    val research = value?.documents?.map { Research.from(it) }
                    onSuccess(research)
                }
            }
    }

    suspend fun findById(researchId: String) = withContext(Dispatchers.IO) {
        try {
            val res = (researchCollection
                .document(researchId)
                .get()
                .await())
                Research.from(res)
        } catch (e: Error) {
            Log.e(TAG, "${e.message}")
            null
        }
    }

    suspend fun updateStatus(id: String?, newStatus: String) = withContext(Dispatchers.IO) {
        if(id != null ) {
            val document = researchCollection
                .whereEqualTo("id", id)
                .get()
                .await()
            researchCollection
                .document(document.documents[0].id)
                .update("status", newStatus)
                .await()

            val res = researchCollection
                .whereEqualTo("id", id)
                .get()
                .await()
                .documents[0]
            Research.from(res)
        } else {
            null
        }
    }
}