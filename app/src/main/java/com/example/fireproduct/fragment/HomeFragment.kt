package com.example.fireproduct.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fireproduct.adapter.ProductAdapter
import com.example.fireproduct.model.Product
import com.example.fireproduct.util.toast
import com.example.fireproduct.R
import com.example.fireproduct.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    private val auth by lazy { FirebaseAuth.getInstance() }
    private val dbRef by lazy { FirebaseDatabase.getInstance().reference }
    private val productAdapter by lazy { ProductAdapter() }
    private val productList = mutableListOf<Product>()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.productsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = productAdapter
        }

        dbRef.child("users").child(auth.currentUser?.uid!!).child("productList")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    productList.clear()
                    for (product in snapshot.children) {
                        productList.add(product.getValue(Product::class.java)!!)
                    }
                    binding.loadingProgressBar.isVisible = false
                    productAdapter.submitList(productList)
                    productAdapter.notifyDataSetChanged()
                    Log.d("@@@", "onDataChange: $productList")
                }

                override fun onCancelled(error: DatabaseError) {
                    toast(error.message)
                }
            })

        binding.createFab.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        productAdapter.onClick =  {
            val bundle = bundleOf("product" to it)
            findNavController().navigate(R.id.action_FirstFragment_to_detailFragment, bundle)
        }
    }
}