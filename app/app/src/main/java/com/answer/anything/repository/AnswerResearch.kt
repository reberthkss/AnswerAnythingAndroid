package com.answer.anything.repository

import android.util.Log
import com.answer.anything.data.AnswerData
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

enum class Collections(val value: String) {RESEARCHS("researchs"), ANSWERED_QUESTIONS("answeredQuestions")}
interface SaveAnsweredQuestionPayload {
    val researchId: String?
    val answerResearchId: String?
    val answeredQuestionId: String?
    val selectedOption: Int
    val prevSelectedOption: Int?
}

interface EndAnswerResearchPayload {
    val researchId: String?
    val answeredResearchId: String?
}
class AnswerResearch {
    private val TAG = "[AnswerResearch]"
    private val firestore = FirebaseFirestore
        .getInstance()
    suspend fun startQuestionnaire(researchId: String, answerData: AnswerData): String? = withContext(Dispatchers.IO) {
        try {
            (firestore
                .collection(Collections.RESEARCHS.value)
                .document(researchId)
                .collection(Collections.ANSWERED_QUESTIONS.value)
                .add(answerData.parseFirebase())
                .await())
                .id
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            null
        }
    }

    suspend fun saveAnsweredQuestion(payload: SaveAnsweredQuestionPayload) = withContext(Dispatchers.IO) {
        if (payload.answerResearchId == null || payload.answeredQuestionId == null || payload.researchId == null) {
            false
        } else {
            try {
                firestore
                    .collection(Collections.RESEARCHS.value)
                    .document(payload.researchId!!)
                    .collection(Collections.ANSWERED_QUESTIONS.value)
                    .document(payload.answerResearchId!!)
                    .update("answeredQuestions", FieldValue.arrayUnion(
                        hashMapOf(
                            "prevSelectedOption" to payload.prevSelectedOption,
                            "questionId" to payload.answeredQuestionId!!,
                            "selectedOption" to payload.selectedOption
                        )
                    ))
                    .await()
                true
            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
                false
            }
        }
    }

    suspend fun endQuestionnaire(payload: EndAnswerResearchPayload) = withContext(Dispatchers.IO) {
        if (payload.answeredResearchId == null || payload.researchId == null) {
            false
        } else {
            try {
                firestore
                    .collection(Collections.RESEARCHS.value)
                    .document(payload.researchId!!)
                    .collection(Collections.ANSWERED_QUESTIONS.value)
                    .document(payload.answeredResearchId!!)
                    .update("status", "done")
                    .await()
                true
            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
                false
            }
        }
    }

}