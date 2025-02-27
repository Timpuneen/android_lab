package com.example.multipageapp.ui

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.multipageapp.AirplaneModeReceiver
import com.example.multipageapp.databinding.FragmentReceiverBinding

class ReceiverFragment : Fragment() {

    private var _binding: FragmentReceiverBinding? = null
    private val binding get() = _binding!!

    private lateinit var airplaneModeReceiver: AirplaneModeReceiver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReceiverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        airplaneModeReceiver = AirplaneModeReceiver { isOn ->
            binding.tvAirplaneMode.text = if (isOn)
                "Airplane mode status: Turned on"
            else
                "Airplane mode status: Turned off"
        }

        val filter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        requireContext().registerReceiver(airplaneModeReceiver, filter)

        updateInitialAirplaneMode()
    }

    private fun updateInitialAirplaneMode() {
        val isAirplaneModeOn = Settings.Global.getInt(
            requireContext().contentResolver,
            Settings.Global.AIRPLANE_MODE_ON,
            0
        ) == 1

        binding.tvAirplaneMode.text = if (isAirplaneModeOn)
            "Airplane mode status: Turned on"
        else
            "Airplane mode status: Turned off"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireContext().unregisterReceiver(airplaneModeReceiver)
        _binding = null
    }
}
