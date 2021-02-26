package com.answer.anything.utils

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.answer.anything.R
import com.answer.anything.data.AnswerUserData
import com.answer.anything.model.AnswerViewModel
import com.answer.anything.model.QRCodeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.register_new_answer.view.*

class RegisterNewAnswerDialog (val onClick: (userData: AnswerUserData) -> Unit): BottomSheetDialogFragment() {
    private lateinit var binding: View
    private val answerResearchViewModel: AnswerViewModel by activityViewModels()
    private val qrCodeViewModel: QRCodeViewModel by activityViewModels()
    private val TAG = "RegisterNewAnswerDialog"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflater.inflate(R.layout.register_new_answer, container, false)
        configEmailAndNameField()
        answerResearchViewModel.getLoadingStatus().observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.content_view.visibility = GONE
                binding.loading_view.visibility = VISIBLE
            } else {
                binding.content_view.visibility = VISIBLE
                binding.loading_view.visibility = GONE
            }
        })

        binding.generate_qr_btn.setOnClickListener {
            binding.user_form_layout.visibility = if (binding.user_form_layout.visibility == VISIBLE) GONE else VISIBLE
            binding.floating_btn_layout.visibility = if (binding.floating_btn_layout.visibility == VISIBLE) GONE else VISIBLE
            binding.qr_code_layout.visibility = if (binding.qr_code_layout.visibility == GONE)  VISIBLE else GONE
            binding.generate_qr_btn.text = if (binding.generate_qr_btn.text == getString(R.string.show_qr_code_helper_text)) getString(R.string.hide_qr_code_helper_text) else getString(R.string.show_qr_code_helper_text)
        }
        val bitmap = qrCodeViewModel.getQRCodeBitmap().value
        binding.qr_code_img_view.setImageBitmap(bitmap)
        return binding.rootView
    }

    fun configEmailAndNameField() {
        binding.start_survey_btn.setOnClickListener {
            val name = binding.name_txt_field.text.toString()
            val email = binding.email_txt_field.text.toString()
            binding.email_txt_field.clearFocus()
            binding.name_txt_field.clearFocus()
            if (isValidName(name) && isValidEmail(email)) {
                onClick(AnswerUserData(name, email))
                this.dismiss()
            } else if (!isValidName(name) && !isValidEmail(email)) {
                binding.name_txt_field_container.error = "Nome inválido!"
                binding.email_txt_field_container.error = "E-mail inválido!"
            } else if (!isValidEmail(email)) {
                binding.email_txt_field_container.error = "E-mail inválido!"
            } else {
                binding.name_txt_field_container.error = "Nome inválido!"
            }
        }

        binding.name_txt_field.onFocusChangeListener = object: View.OnFocusChangeListener {
            override fun onFocusChange(p0: View?, p1: Boolean) {
                binding.name_txt_field_container.error = null
                binding.email_txt_field_container.error = null
            }
        }

        binding.email_txt_field.onFocusChangeListener = object: View.OnFocusChangeListener {
            override fun onFocusChange(p0: View?, p1: Boolean) {
                binding.name_txt_field_container.error = null
                binding.email_txt_field_container.error = null
            }
        }
    }

    fun isValidName(name: String?): Boolean {
        if (name == null) return false
        val regex = "^[a-zA-Z]{4,}(?: [a-zA-Z]+){0,2}\$".toRegex()
        return regex.matches(name)
    }

    fun isValidEmail(email: String?): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }



}