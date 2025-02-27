package com.example.multipageapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.multipageapp.R
import com.example.multipageapp.databinding.FragmentMainBinding


class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()
        binding.btnIntents.setOnClickListener {
            navController.navigate(R.id.intentsFragment)

        }
        binding.btnService.setOnClickListener {
            navController.navigate(R.id.serviceFragment)
        }
        binding.btnReceiver.setOnClickListener {
            navController.navigate(R.id.receiverFragment)
        }
        binding.btnProvider.setOnClickListener {
            navController.navigate(R.id.providerFragment)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
