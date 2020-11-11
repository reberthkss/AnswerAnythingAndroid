package com.answer.anything.data

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot

enum class AnswerStatus(val value: String) {PROGRESS("progress"), DONE("done")}

data class AnswerData(
    val userData: AnswerUserData,
    val answeredQuestions: MutableList<AnsweredQuestion> = mutableListOf(),
    val status: AnswerStatus = AnswerStatus.PROGRESS
) {
    fun parseFirebase(): HashMap<String, Any> {
        return hashMapOf<String, Any>(
            "userData" to this.userData.parseFirebase(),
            "answeredQuestions" to this.answeredQuestions.map { it.parseFirebase() },
            "status" to this.status.value
        )
    }

    /*fun fromFirebase(document: DocumentSnapshot): AnswerData {
//        val userData = AnswerUserData(document.getString("userData"))
        Log.d("AnswerData", "${document.getString("userData")}")
        return AnswerData(

        )
    }*/
}