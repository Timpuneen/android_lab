package com.example.multipageapp.ui

import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.multipageapp.databinding.FragmentProviderBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.Manifest
import com.example.multipageapp.ProviderAdapter


class ProviderFragment : Fragment() {

    private lateinit var binding: FragmentProviderBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProviderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (checkCalendarPermission()) {
            loadCalendarEvents()
        } else {
            requestCalendarPermission()
        }
    }

    private fun checkCalendarPermission(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCalendarPermission() {
        requestPermissions(arrayOf(Manifest.permission.READ_CALENDAR), REQUEST_CALENDAR_PERMISSION)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CALENDAR_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadCalendarEvents()
            } else {
                Toast.makeText(requireContext(), "No permission to read calendar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadCalendarEvents() {
        val events = getCalendarEvents()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = ProviderAdapter(events)
    }

    private fun getCalendarEvents(): List<String> {
        val events = mutableListOf<String>()
        val uri: Uri = CalendarContract.Events.CONTENT_URI
        val projection = arrayOf(CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART)
        val cursor: Cursor? = requireContext().contentResolver.query(uri, projection, null, null, "${CalendarContract.Events.DTSTART} ASC")

        cursor?.use {
            while (it.moveToNext()) {
                val title = it.getString(0)
                val date = Date(it.getLong(1))
                events.add("$title - ${SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(date)}")
            }
        }
        return events
    }

    companion object {
        private const val REQUEST_CALENDAR_PERMISSION = 1001
    }
}
