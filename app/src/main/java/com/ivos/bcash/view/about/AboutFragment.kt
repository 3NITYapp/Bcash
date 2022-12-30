package com.ivos.bcash.view.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ivos.bcash.databinding.FragmentAboutBinding


class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding: FragmentAboutBinding
        get() = _binding ?: throw java.lang.RuntimeException("Binding is empty")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}