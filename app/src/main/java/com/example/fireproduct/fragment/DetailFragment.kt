package com.example.fireproduct.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.fireproduct.model.Product
import com.example.fireproduct.util.toast
import com.example.fireproduct.databinding.FragmentDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private var product: Product? = null
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val dbRef by lazy {
        FirebaseDatabase.getInstance().reference.child("users").child(auth.currentUser?.uid!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        product = arguments?.getParcelable("product")
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.productNameEditText.setText(product?.name)
        binding.productPriceEditText.setText(product?.price)
        binding.updateButton.setOnClickListener {
            val name = binding.productNameEditText.text.toString().trim()
            val price = binding.productPriceEditText.text.toString().trim()
            if (name.isNotBlank() && price.isNotBlank()) {
                dbRef.child("productList").child(product?.name!!).setValue(Product(name, price))
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            toast("Updated")
                            findNavController().popBackStack()
                        } else {
                            toast(it.exception?.message.toString())
                        }
                    }
            }
        }
        binding.deleteButton.setOnClickListener {
            dbRef.child("productList")
                .child(product?.name!!)
                .removeValue().addOnCompleteListener {
                    if (it.isSuccessful) {
                        toast("Deleted")
                        findNavController().popBackStack()
                    } else {
                        toast(it.exception?.message.toString())
                    }
                }
        }
    }
}