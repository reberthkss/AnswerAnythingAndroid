package com.answer.anything.data

data class ResearchQuestion (
val id: String,
val question: String,
val options: HashMap<Int, String>,
var selectedOption: Int? = null
) {
    fun isAnswered(): Boolean {
        return this.selectedOption != null;
    }

    companion object {
        fun from(doc: HashMap<String, String>): ResearchQuestion {
            val options: HashMap<Int, String> = hashMapOf();
            (doc["options"] as HashMap<String, String>).forEach {
                options.put(it.key.toInt(), it.value)
            }
            return ResearchQuestion(
                doc["id"] ?: "",
                doc["question"] ?: "",
                options ?: hashMapOf(),
                (doc["selectedOption"] as String).toInt()
            )
        }
    }
}