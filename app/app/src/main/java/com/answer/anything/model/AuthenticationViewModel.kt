package com.answer.anything.model

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.answer.anything.data.AuthStatus
import com.answer.anything.manager.FacebookAuthenticationManager
import com.answer.anything.manager.GoogleAuthenticationManager
import com.answer.anything.manager.HandleResultListener
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthenticationViewModel() : ViewModel() {
    private val TAG = "AuthenticationViewModel"
    private val authStatus = MutableLiveData<AuthStatus>(AuthStatus.UNAUTH)
    private val authUser = MutableLiveData<FirebaseUser?>(null)
    private lateinit var googleAuthManager: GoogleAuthenticationManager
    private lateinit var facebookAuthManager: FacebookAuthenticationManager
    private lateinit var auth: FirebaseAuth


    fun configAuth(activity: AppCompatActivity) {
        googleAuthManager = GoogleAuthenticationManager(activity);
        facebookAuthManager = FacebookAuthenticationManager()
        googleAuthManager.init()
        facebookAuthManager.init()
        auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) authStatus.value = AuthStatus.AUTH else authStatus.value = AuthStatus.UNAUTH
    }

    fun signIn(callback: (client: GoogleSignInClient)->Unit) {
        googleAuthManager.signIn {
            callback(it)
        }
    }

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
        LoginManager.getInstance().logOut()
        authStatus.value = AuthStatus.UNAUTH
    }

    fun isAuthenticated(): Boolean {
        return authStatus.value == AuthStatus.AUTH
    }

    fun finishAuthentication(task: Task<GoogleSignInAccount>, callback: HandleResultListener) {
        googleAuthManager.handleSignInResult(task, object: HandleResultListener {
            override fun onSuccess() {
                authStatus.value = AuthStatus.AUTH
                callback.onSuccess()
            }

            override fun onFailure() {
                callback.onFailure()
            }
        })
    }

    fun handleFacebookAccessToken(token: AccessToken, activity: AppCompatActivity) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "Successfully signed in")
                    authStatus.value = AuthStatus.AUTH
                } else {
                  Log.d(TAG, "Authentication failed!")
                    authStatus.value = AuthStatus.UNAUTH
                }
            }
    }

    fun getAuthStatus(): LiveData<AuthStatus> {
        return authStatus
    }

    fun setAuthStatus(newStatus: AuthStatus) {
        authStatus.value = newStatus
    }

    fun getAuthenticatedUser(): LiveData<FirebaseUser?> {
        return authUser
    }

    fun getCallbackManager(): CallbackManager = facebookAuthManager.getCallbackManager()
}