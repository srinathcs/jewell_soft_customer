package com.sgs.manthara.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.sgs.manthara.R
import com.sgs.manthara.activity.DashBoardActivity
import com.sgs.manthara.databinding.FragmentTickBinding

class TickFragment : Fragment() {
   private lateinit var binding:FragmentTickBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentTickBinding.inflate(inflater,container,false)
        binding.btnOk.setOnClickListener {
            val int = Intent(requireActivity(),DashBoardActivity::class.java)
            startActivity(int)
            requireActivity().finish()
        }
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(R.id.viewPage)
        }
        return binding.root
    }

}