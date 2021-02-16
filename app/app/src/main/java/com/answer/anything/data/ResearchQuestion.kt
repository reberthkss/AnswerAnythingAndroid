package com.answer.anything.data

import android.util.Log

data class ResearchQuestion (
val id: String,
val question: String,
val options: HashMap<Int, String>,
var selectedOption: Int? = null,
var prevSelectedOption: Int? = null
) {
    fun isAnswered(): Boolean {
        return this.selectedOption != null;
    }

    companion object {
        fun from(doc: HashMap<String, Any>): ResearchQuestion {
            val options: HashMap<Int, String> = hashMapOf();
            (doc["options"] as List<HashMap<String, String>>).forEach {
                it.forEach {
                    options.put(it.key.toInt(), it.value)
                }
            }
            return ResearchQuestion(
                doc["id"] as String ?: "",
                doc["question"] as String ?: "",
                options ?: hashMapOf()
            )
        }
    }
}