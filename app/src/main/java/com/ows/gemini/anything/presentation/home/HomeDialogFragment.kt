package com.ows.gemini.anything.presentation.home

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ows.gemini.anything.R
import com.ows.gemini.anything.databinding.FragmentHomeDialogBinding

class HomeDialogFragment(
    private val title: String,
    private val subTitle: String,
    private val onClick: () -> Unit = {},
) : DialogFragment() {
    private var _binding: FragmentHomeDialogBinding? = null
    val binding: FragmentHomeDialogBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeDialogBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(
            ColorDrawable(resources.getColor(R.color.transparent, null)),
        )
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundColor(resources.getColor(R.color.black, null))
        binding.tvTitle.text = title
        binding.tvSubtitle.text = subTitle
        binding.btnConfirm.setOnClickListener {
            onClick()
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "HomeDialogFragment"
    }
}
