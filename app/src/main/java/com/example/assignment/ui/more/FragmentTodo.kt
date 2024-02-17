package com.example.assignment.ui.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.assignment.databinding.FragmentCommonComingSoonBinding

class FragmentTodo : Fragment() {

    private var _binding: FragmentCommonComingSoonBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommonComingSoonBinding.inflate(inflater, container, false)

        binding.titleLayout.tvTitle.text = "To-Do"
        return  binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}