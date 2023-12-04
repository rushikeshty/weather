package com.example.androidassignment.assignment.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.androidassignment.R
import com.example.androidassignment.assignment.SessionManager
import com.example.androidassignment.assignment.firebase.FirebaseAuthentication
import com.example.androidassignment.assignment.firebase.FirebaseListener
import com.example.androidassignment.databinding.FragmentLoginBinding


open class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    var emailStr = ""
    private var valid = false
    private lateinit var firebaseAuthentication: FirebaseAuthentication


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = "Welcome"
        actionBar?.show()
        val sessionManager = SessionManager(requireContext())


        firebaseAuthentication = FirebaseAuthentication()
        binding.loading.visibility = View.VISIBLE
        firebaseAuthentication.getCurrentUser(object : FirebaseListener {

            override fun onCompleteListener() {
                binding.loading.visibility = View.INVISIBLE
                findNavController().navigate(R.id.action_loginFragment_to_weather)
            }

            override fun onFailureListener(error: String) {
                binding.loading.visibility = View.INVISIBLE
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()

            }

        })
        binding.register.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

                if ((p0?.length!! > 0) && (p0.length > 7)) {
                    return
                } else {
                    binding.password.error =
                        context?.getString(R.string.password_should_contain_8_characters)
                }
            }

        })

        binding.username.apply {
            var currChar = ' '
            addTextChangedListener(object : TextWatcher {

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    emailStr = ""
                    if (p1 > 0 && p0?.length!! > 1) {
                        currChar = p0[p1 - 1]
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
                        binding.username.error =
                            context?.getString(R.string.email_address_should_contain) + "{$currChar}"
                    }
                }

            })
            onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
                emailStr.trim()
                if (!hasFocus) {
                    if (emailStr.matches(Patterns.EMAIL_ADDRESS.toRegex())) {
                        if (binding.password.text?.isBlank() == true) {
                            showToast(context.getString(R.string.enter_the_password))
                        }

                        valid = true

                    } else {
                        if (binding.username.text != null) {
                            val userText = binding.username.text
                            if (userText?.isEmpty() == true) {
                                binding.username.error =
                                    context.getString(R.string.email_address_must_be_required)
                                showToast(context.getString(R.string.email_address_must_be_required))
                            } else {
                                if (userText?.isNotEmpty() == true && !userText.toString()
                                        .contains("@")
                                ) {
                                    binding.username.error =
                                        context.getString(R.string.email_address_should_contain)
                                    showToast(context.getString(R.string.email_address_should_contain))
                                } else {
                                    val idx = userText.toString().indexOf("@")
                                    if ((idx < userText?.length!! - 1)) {
                                        if ((userText[idx + 1] == '.') || (userText[idx + 1] == ' ')
                                        ) {
                                            binding.username.error =
                                                context.getString(R.string.email_address_should_contain)
                                            showToast(context.getString(R.string.email_address_should_contain))

                                        }

                                    } else {
                                        binding.username.error =
                                            context.getString(R.string.email_address_should_contain)
                                        showToast(context.getString(R.string.email_address_should_contain))

                                    }

                                }
                            }
                        }
                    }
                }
            }
        }

        binding.login.setOnClickListener {

            Toast.makeText(requireContext(), valid.toString(), Toast.LENGTH_SHORT).show()
            if (valid) {
                setToLowAlpha()
                hideInputKeyBoard()
                binding.loading.visibility = View.VISIBLE
                if (binding.username.text.toString() != "" && binding.password.text != null) {
                    //  sessionManager.createLoginRegisterSession("name",binding.username.text.toString(),binding.password.text.toString())
                    firebaseAuthentication.signInToFirebase(
                        emailStr,
                        binding.password.text.toString(),
                        object : FirebaseListener {
                            override fun onCompleteListener() {
                                setToHighAlpha()
                                binding.loading.visibility = View.INVISIBLE
                                findNavController().navigate(R.id.action_loginFragment_to_weather)
                                showToast(getString(R.string.successfully_logged_in))
                            }
                            override fun onFailureListener(error: String) {
                                setToHighAlpha()
                                binding.loading.visibility = View.INVISIBLE
                                showToast("Email or password incorrect  ")

                            }
                        }
                    )

                } else {
                    setToHighAlpha()
                    context?.getString(R.string.username_or_password_incorrect)
                        ?.let { it1 -> showToast(it1) }
                }
            } else {
                showToast(getString(R.string.username_or_password_incorrect))
            }
        }

    }

    private fun setToHighAlpha() {
        binding.apply {
            textInputLayout.alpha = 1f
            textView3.alpha = 1f
            textInputLayout2.alpha = 1f
            textView4.alpha = 1f
            username.alpha = 1f
            password.alpha = 1f
            register.alpha = 1f
            login.alpha = 1f
        }
    }

    private fun setToLowAlpha() {
        binding.apply {
            textInputLayout.alpha = 0.3f
            textView3.alpha = 0.3f
            textInputLayout2.alpha = 0.3f
            textView4.alpha = 0.3f
            username.alpha = 0.3f
            password.alpha = 0.3f
            login.alpha = 0.3f
            register.alpha = 0.3f
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }


}
