package com.answer.anything.fragments.configuration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.answer.anything.databinding.ConfigurationsFragmentBinding
import com.answer.anything.model.AuthenticationViewModel

class ConfigurationsFragment : Fragment() {
    private lateinit var binding: ConfigurationsFragmentBinding
    private val googleAuthenticationViewModel: AuthenticationViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ConfigurationsFragmentBinding.inflate(inflater, container, false);
        binding.signOutButton.setOnClickListener {
            googleAuthenticationViewModel.signOut()
        }
        return binding.root;
    }
}