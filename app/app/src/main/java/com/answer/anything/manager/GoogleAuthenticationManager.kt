package com.answer.anything.manager

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.answer.anything.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

interface HandleResultListener {
    fun onSuccess(): Unit
    fun onFailure(): Unit
}

class GoogleAuthenticationManager(val activity: AppCompatActivity) {
    companion object {
        val RC_SIGN_IN = 9001
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    fun init() {
        val clientId = activity.getString(R.string.google_oauth_id)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(clientId)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
        auth = FirebaseAuth.getInstance()
    }

    fun signIn(callback: (client: GoogleSignInClient) -> Unit) {
        callback(mGoogleSignInClient)
    }

    fun handleSignInResult(task: Task<GoogleSignInAccount>, callbacks: HandleResultListener) {
        try {
            val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
            this.firebaseAuthWithGoogle(account?.idToken, object: HandleResultListener {
                override fun onSuccess() {
                    callbacks.onSuccess()
                }

                override fun onFailure() {
                    callbacks.onFailure()
                }
            })
            // Signed in successfully, show authenticated UI.

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("GoogleAuthManager", "signInResult:failed code=" + e.statusCode)
            callbacks.onFailure()
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?, callbacks: HandleResultListener) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("GoogleAuthManager", "signInWithCredential:success")
                    val user = auth.currentUser
                    callbacks.onSuccess()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("GoogleAuthManager", "signInWithCredential:failure", task.exception)
                    callbacks.onFailure()
                }

            }
    }

    fun getUserAuthenticated(): FirebaseUser? {
        return auth.currentUser
    }

    fun isAuthenticated(): Boolean {
        return auth.currentUser != null
    }

    fun signOut() {
        auth.signOut()
    }
}