package com.answer.anything.data

data class AnsweredQuestion(
    val questionId: String,
    val selectedOption: Int,
    val prevSelectedOption: Int?
) {
    fun parseFirebase(): HashMap<String, *> {
        return hashMapOf(
            "prevSelectedOption" to this.prevSelectedOption,
            "questionId" to this.questionId,
            "selectedOption" to this.selectedOption
        )
    }
}