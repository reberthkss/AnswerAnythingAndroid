package com.answer.anything.data

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

data class Research (
val id: String,
val title: String,
val subtitle: String,
val description: String,
val questions: List<ResearchQuestion>,
val status: ResearchStatus?,
val roles: HashMap<String, String>,
) {
    companion object {
        fun from(doc: DocumentSnapshot): Research {
            val status = doc.getString("status")!!;
            val questions = doc.get("questions");
//            Log.d("Researchs", "questions => ${questions}");
            return Research(
                doc.id,
                doc.getString("title") ?: "",
                doc.getString("subtitle") ?: "",
                doc.getString("description") ?: "",
                getQuestionsList(questions),
                getStatus(doc, status),
                doc.get("roles") as HashMap<String, String>
            )
        }

        private fun getStatus(
            doc: DocumentSnapshot,
            status: String
        ) = if (doc.getString("status") != null) ResearchStatus.valueOf(status.toUpperCase()) else ResearchStatus.OPEN

        private fun getQuestionsList(questions: Any?): List<ResearchQuestion> {
            return if (questions != null) (questions as List<HashMap<String, Any>>).map {
                ResearchQuestion.from(it)
            } else listOf()
        }
    }
}