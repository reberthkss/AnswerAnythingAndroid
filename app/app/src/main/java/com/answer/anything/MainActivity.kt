package com.answer.anything

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.answer.anything.data.GoogleAuthStatus
import com.answer.anything.databinding.ActivityMainBinding
import com.answer.anything.model.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings


class MainActivity : AppCompatActivity() {
    companion object {
        val TAG = "MainActivity"
    }
    lateinit var firestore: FirebaseFirestore
    private lateinit var binding: ActivityMainBinding
    private val researchViewModel: ResearchViewModel by viewModels()
    private val googleAuthViewModel: AuthenticationViewModel by viewModels()
    private val answerResearchViewModel: AnswerViewModel by viewModels()
    private val qrCodeViewModel: QRCodeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomBar()
        googleAuthViewModel.configAuth(this)

        if (BuildConfig.DEBUG) {
            firestore = FirebaseFirestore.getInstance()
            firestore.useEmulator("10.0.2.2", 8080)

            val firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build()
            firestore.firestoreSettings = firestoreSettings
        }
        answerResearchViewModel.getStatusOfAnswer().observe(this, Observer {
            when (it) {
                AnswerViewModelStatus.GETTING_USER_DATA -> binding.bottomToolBar.selectedItemId = R.id.researchs
                AnswerViewModelStatus.ANSWERING -> binding.bottomToolBar.selectedItemId = R.id.searching
                AnswerViewModelStatus.DONE -> binding.bottomToolBar.selectedItemId = R.id.finished
            }
        })

        googleAuthViewModel.getAuthStatus().observe(this, Observer {
            if (it == GoogleAuthStatus.AUTH) {
                binding.bottomToolBar.selectedItemId = R.id.researchs;
                researchViewModel.config();
            } else {
                binding.navHostFragmentContainer.findNavController().setGraph(R.navigation.authentication_navigation)
                binding.topToolbar.visibility = GONE
                binding.bottomToolBar.visibility = GONE
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        researchViewModel.unsubscribe()
    }

    override fun onStart() {
        super.onStart()
        if (googleAuthViewModel.isAuthenticated()) {
            binding.bottomToolBar.selectedItemId = R.id.researchs
        }
    }

    private fun updateGraph(graph: Int) {
        binding.navHostFragmentContainer.findNavController().setGraph(graph)
    }

    private fun setBottomBar() {
        binding.bottomToolBar.setOnNavigationItemSelectedListener(object :
            BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(p0: MenuItem): Boolean {
                if (googleAuthViewModel.isAuthenticated()) {
                    binding.topToolbar.visibility = VISIBLE
                    binding.bottomToolBar.visibility = VISIBLE
                } else {
                    updateGraph(R.navigation.authentication_navigation)
                    binding.topToolbar.visibility = GONE
                    binding.bottomToolBar.visibility = GONE
                    return false
                }
                when (p0.itemId) {
                    R.id.researchs -> {
                        researchViewModel.filterOpenResearchs()
                        binding.toolbarTitle.text = getString(R.string.dashboard_toolbar_label)
                        updateGraph(R.navigation.research_tab_navigation)
                        return true
                    }
                    R.id.searching -> {
                        binding.toolbarTitle.text = getString(R.string.researchs_toolbar_label)
                        updateGraph(R.navigation.progress_tab_navigation)
                        return true
                    }
                    R.id.finished -> {
                        researchViewModel.filterClosedResearchs()
                        binding.toolbarTitle.text = getString(R.string.dones_researchs_toolbar_label)
                        updateGraph(R.navigation.research_tab_navigation)
                        return true
                    }
                    R.id.configurations -> {
                        binding.toolbarTitle.text = getString(R.string.config_toolbar_label)
                        updateGraph(R.navigation.configuration_navigation)
                        return true
                    }
                    else -> {
                        return false
                    }
                }
            }
        })
    }
}
