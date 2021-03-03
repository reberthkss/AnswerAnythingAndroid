package com.answer.anything.fragments.authentication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.answer.anything.R
import com.answer.anything.databinding.AuthenticationFragmentBinding
import com.answer.anything.manager.FacebookAuthenticationManager.Companion.EMAIL
import com.answer.anything.manager.FacebookAuthenticationManager.Companion.PROFILE
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
import java.util.*


class AuthenticationFragment : Fragment() {
    private val TAG = "AuthenticationFragment"
    private lateinit var binding: AuthenticationFragmentBinding
    private val authenticationViewModel: AuthenticationViewModel by activityViewModels()
    private lateinit var callbackManager: CallbackManager
    private lateinit var runnable: Runnable
    private  val handler: Handler = Handler()
    private val timer = Timer()

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
        callbackManager = authenticationViewModel.getCallbackManager()
        binding.facebookLoginButton.fragment = this
        binding.facebookLoginButton.setPermissions(listOf(EMAIL, PROFILE))
        binding.facebookLoginButton.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    authenticationViewModel.handleFacebookAccessToken(
                        loginResult!!.accessToken,
                        activity as AppCompatActivity
                    )
                }

                override fun onCancel() {
                    Log.d(TAG, "User canceled")
                }

                override fun onError(exception: FacebookException) {
                    Log.d(TAG, "Error => ${exception.message}")
                }
            })

        val texts = listOf(
            getString(R.string.search_anywhere_msg),
            getString(R.string.are_your_customers_satisfied),
            getString(R.string.are_you_making_a_market_research)
        )
        binding.pager.adapter = TextApresentationViewPager(this, texts)
        binding.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.pager.setCurrentItem(position, true)
            }
        })

        runnable = Runnable {
            var page = binding.pager.currentItem
            if (page == texts.size-1) {
                page = 0
            } else {
                page++
            }
            binding.pager.setCurrentItem(page, true)
        }



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

    override fun onResume() {
        timer.scheduleAtFixedRate(object: TimerTask() {
            override fun run() {
                handler.post(runnable)
            }

        }, 3000, 5000)
        super.onResume()
    }

    override fun onPause() {
        timer.purge()
        super.onPause()
    }
}