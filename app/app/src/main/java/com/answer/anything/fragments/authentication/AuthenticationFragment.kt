package com.answer.anything.fragments.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.answer.anything.databinding.AuthenticationFragmentBinding
import com.answer.anything.manager.FacebookAuthenticationManager.Companion.EMAIL
import com.answer.anything.manager.GoogleAuthenticationManager
import com.answer.anything.manager.HandleResultListener
import com.answer.anything.model.AuthenticationViewModel
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task


class AuthenticationFragment : Fragment() {
    private lateinit var binding: AuthenticationFragmentBinding
    private val authenticationViewModel: AuthenticationViewModel by activityViewModels()
    private  lateinit var callbackManager: CallbackManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AuthenticationFragmentBinding.inflate(inflater, container, false);
        binding.googleSignIn.setOnClickListener {
            authenticationViewModel.signIn {
                val signInIntent: Intent = it.signInIntent
                startActivityForResult(signInIntent, GoogleAuthenticationManager.RC_SIGN_IN)
            }
        }
        binding.facebookLoginButton.setPermissions(listOf(EMAIL))
        callbackManager = authenticationViewModel.getCallbackManager()
        binding.facebookLoginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
                // App code
            }

            override fun onCancel() {
                // App code
            }

            override fun onError(exception: FacebookException) {
                // App code
            }
        })
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GoogleAuthenticationManager.RC_SIGN_IN) {
            data as Intent
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            authenticationViewModel.finishAuthentication(task, object : HandleResultListener {
                override fun onSuccess() {
                    Log.d("AuthenticationFragment", "[onSuccess] Success sign in");
                }

                override fun onFailure() {
                    Log.d("AuthenticationFragment", "[onFailure] Failure sign in");
                }
            })
        }
    }
}