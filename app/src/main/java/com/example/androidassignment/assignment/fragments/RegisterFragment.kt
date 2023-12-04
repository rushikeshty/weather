package com.example.androidassignment.assignment.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.androidassignment.R
import com.example.androidassignment.assignment.SessionManager
import com.example.androidassignment.assignment.firebase.FirebaseAuthentication
import com.example.androidassignment.assignment.firebase.FirebaseListener
import com.example.androidassignment.databinding.FragmentRegisterBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {
    private lateinit var _binding: FragmentRegisterBinding
    private var emailStr = ""
    private var valid = false
    private lateinit var firebaseAuthentication: FirebaseAuthentication
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuthentication = FirebaseAuthentication()

        _binding.password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length!! > 7) {
                    return
                } else {

                    _binding.password.error =
                        getString(R.string.password_should_contain_8_characters)
                }
            }

        })

        _binding.username.apply {
            var currChar = ' '
            addTextChangedListener(object : TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {

                    emailStr = ""
                    if (start > 0 && p0?.length!! > 1) {
                        currChar = p0[start - 1]
                    }

                }

                override fun afterTextChanged(p0: Editable?) {
                    p0?.forEach {
                        emailStr += it
                    }
                    if (!emailStr.contains("!#\$%&'*+-/=?^_`{|}~") && !emailStr.contains("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\$")) {
                        if (currChar == ' ') {
                            return
                        }

                    } else {
                        showAnimation(_binding.username)
                        _binding.username.error =
                            context.getString(R.string.email_address_should_not_contain, currChar)
                    }
                }

            })
            onFocusChangeListener = View.OnFocusChangeListener { view, hasfocus ->

                if (!hasfocus) {
                    /*^([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6})?$
                    */
                    if (emailStr.matches(Patterns.EMAIL_ADDRESS.toRegex())) {
                        if (_binding.password.text?.isBlank() == true) {
                            showToast(context.getString(R.string.enter_the_password))
                        }

                        valid = true

                    } else {
                        if (_binding.username.text != null) {
                            val userText = _binding.username.text
                            if (userText?.isEmpty() == true) {
                                showAnimation(_binding.username)
                                _binding.username.error =
                                    context.getString(R.string.email_address_must_be_required)
                                showToast(context.getString(R.string.email_address_must_be_required))
                            } else {
                                if (userText?.isNotEmpty() == true && !userText.toString()
                                        .contains("@")
                                ) {
                                    showAnimation(_binding.username)
                                    _binding.username.error =
                                        context.getString(R.string.email_address_should_contain)
                                    showToast(context.getString(R.string.email_address_should_contain))
                                } else {
                                    val idx = userText.toString().indexOf("@")
                                    if ((idx < userText?.length!! - 1)) {
                                        if ((userText[idx + 1] == '.') || (userText.get(
                                                idx + 1
                                            ) == ' ')
                                        ) {
                                            showAnimation(_binding.username)
                                            _binding.username.error =
                                                context.getString(R.string.email_address_should_contain_domain_name_after)
                                            showToast(context.getString(R.string.email_address_should_contain_domain_name_after))

                                        }

                                    } else {
                                        showAnimation(_binding.username)
                                        _binding.username.error =
                                            context.getString(R.string.email_address_should_contain_domain_name_after)
                                        showToast(context.getString(R.string.email_address_should_contain_domain_name_after))

                                    }

                                }
                            }
                        }
                    }
                }
            }

        }
        _binding.login.setOnClickListener {
            hideInputKeyBoard()
            val name = _binding.name.text.toString()
            val email = _binding.username.text.toString()
            val password = _binding.password.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && valid) {
                _binding.loading.visibility = View.VISIBLE
                setToLowAlpha()
                firebaseAuthentication.signUpToFirebase(email, password, object : FirebaseListener {
                    override fun onCompleteListener() {
                        _binding.loading.visibility = View.INVISIBLE
                        findNavController().navigate(R.id.action_registerFragment_to_weather)
                    }

                    override fun onFailureListener(error: String) {
                        _binding.loading.visibility = View.INVISIBLE
                        setToLowAlpha()
                        Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
                    }

                })

            } else {
                setToHighAlpha()
                Toast.makeText(
                    requireContext(),
                    getString(R.string.all_fields_are_required), Toast.LENGTH_SHORT
                )
                    .show()
            }
        }

    }

    private fun showAnimation(view: View) {
        YoYo.with(Techniques.Shake).apply {
            delay(500)
            duration(1000)
            playOn(view)
        }
    }

    private fun setToHighAlpha() {
        _binding.apply {
            textInputLayout.alpha = 1f
            textView3.alpha = 1f
            textInputLayout2.alpha = 1f
            textView4.alpha = 1f
            username.alpha = 1f
            password.alpha = 1f
            login.alpha = 1f
            name.alpha = 1f
        }
    }

    private fun setToLowAlpha() {
        _binding.apply {
            textInputLayout.alpha = 0.3f
            textView3.alpha = 0.3f
            textInputLayout2.alpha = 0.3f
            textView4.alpha = 0.3f
            username.alpha = 0.3f
            password.alpha = 0.3f
            login.alpha = 0.3f
            name.alpha = 0.3f
        }


    }

    private fun hideInputKeyBoard() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = requireActivity().currentFocus
        if (view == null) {
            view = View(activity)
        }
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }


    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()

    }

}