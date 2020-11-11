package com.answer.anything.model

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.answer.anything.data.GoogleAuthStatus
import com.answer.anything.manager.GoogleAuthenticationManager
import com.answer.anything.manager.HandleResultListener
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser

class AuthenticationViewModel() : ViewModel() {
    private val authStatus = MutableLiveData<GoogleAuthStatus>(GoogleAuthStatus.UNAUTH);
    private val authUser = MutableLiveData<FirebaseUser?>(null)
    private lateinit var googleAuthManager: GoogleAuthenticationManager

    fun configAuth(activity: AppCompatActivity) {
        googleAuthManager = GoogleAuthenticationManager(activity);
        googleAuthManager.init()
        if (googleAuthManager.isAuthenticated()) {
            authStatus.value = GoogleAuthStatus.AUTH
        } else {
            authStatus.value = GoogleAuthStatus.UNAUTH
        }
    }

    fun signIn(callback: (client: GoogleSignInClient)->Unit) {
        googleAuthManager.signIn {
            callback(it)
        }
    }

    fun signOut() {
        googleAuthManager.signOut();
        authStatus.value = GoogleAuthStatus.UNAUTH;
    }

    fun isAuthenticated(): Boolean {
        return authStatus.value == GoogleAuthStatus.AUTH
    }

    fun finishAuthentication(task: Task<GoogleSignInAccount>, callback: HandleResultListener) {
        googleAuthManager.handleSignInResult(task, object: HandleResultListener {
            override fun onSuccess() {
                authStatus.value = GoogleAuthStatus.AUTH
                callback.onSuccess()
            }

            override fun onFailure() {
                callback.onFailure()
            }
        })
    }

    fun getAuthStatus(): LiveData<GoogleAuthStatus> {
        return authStatus
    }

    fun getAuthenticatedUser(): LiveData<FirebaseUser?> {
        return authUser
    }
}