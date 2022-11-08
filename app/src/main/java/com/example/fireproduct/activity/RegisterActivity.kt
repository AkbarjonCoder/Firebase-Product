package com.example.fireproduct.activity

import android.os.Bundle
import com.example.fireproduct.model.User
import com.example.fireproduct.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : BaseActivity() {
    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().reference

        binding.backButton.setOnClickListener {
            finish()
        }
        binding.registerButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            if (validate(email, password, binding.emailInput, binding.passwordInput)) {
                binding.loadingProgressBar.isIndeterminate = true
                register(name, email, password)
            }
        }
    }

    private fun register(name: String, email: String, password: String) {
        binding.loadingProgressBar.isIndeterminate = false
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    addToDatabase(name, email, password, auth.currentUser?.uid!!)
                    toast("Sign up success")
                    intent(this, MainActivity())
                    finish()
                } else {
                    toast(it.exception?.message.toString())
                }
            }
    }
    private fun addToDatabase(name: String, email: String, password: String, uid: String) {
        dbRef.child("users").child(uid).setValue(User(name, email, password, uid))
    }
}