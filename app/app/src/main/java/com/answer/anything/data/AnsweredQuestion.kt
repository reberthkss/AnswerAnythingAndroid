package com.answer.anything.data

data class AnsweredQuestion(
    val questionId: String,
    val selectedOption: Int
) {
    fun parseFirebase(): HashMap<String, Any> {
        return hashMapOf<String, Any>(
            "questionId" to this.questionId,
            "selectedOption" to this.selectedOption
        )
    }
}