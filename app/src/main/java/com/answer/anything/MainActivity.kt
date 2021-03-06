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
import com.answer.anything.data.Research
import com.answer.anything.data.ResearchStatus
import com.answer.anything.databinding.ActivityMainBinding
import com.answer.anything.model.AuthenticationViewModel
import com.answer.anything.model.InProgressResearchViewModel
import com.answer.anything.model.ResearchViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    companion object {
        val TAG = "MainActivity"
    }
    private lateinit var binding: ActivityMainBinding
    private val researchViewModel: ResearchViewModel by viewModels()
    private val inProgressResearchViewModel: InProgressResearchViewModel by viewModels()
    private val googleAuthViewModel: AuthenticationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomBar()
        googleAuthViewModel.configAuth(this)

        inProgressResearchViewModel.getResearch().observe(this, Observer {it: Research? ->
            if (it == null) {
                binding.bottomToolBar.selectedItemId = R.id.researchs
            } else if (it.status == ResearchStatus.CLOSED) {
                binding.bottomToolBar.selectedItemId = R.id.finished;
            } else {
                binding.bottomToolBar.selectedItemId = R.id.searching
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
                        binding.toolbarTitle.text = "Dashboard"
                        updateGraph(R.navigation.research_tab_navigation)
                        return true
                    }
                    R.id.searching -> {
                        binding.toolbarTitle.text = "Researchs"
                        updateGraph(R.navigation.progress_tab_navigation)
                        return true
                    }
                    R.id.finished -> {
                        researchViewModel.filterClosedResearchs()
                        binding.toolbarTitle.text = "Dones"
                        updateGraph(R.navigation.research_tab_navigation)
                        return true
                    }
                    R.id.configurations -> {
                        binding.toolbarTitle.text = "Configurações"
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
