package com.answer.anything.data

data class AnswerUserData(
   val email: String,
   val name: String,
) {
   fun parseFirebase(): HashMap<String, Any> {
      return hashMapOf<String, Any>(
         "email" to this.email,
         "name" to this.name
      )
   }
}