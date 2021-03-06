package com.example.easycarpoolapp.auth.join

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.easycarpoolapp.R
import com.example.easycarpoolapp.databinding.FragmentJoinPhoneVerifyBinding
import com.google.firebase.auth.PhoneAuthProvider


class JoinPhoneVerifyFragment private constructor(): Fragment() {

    interface CallBacks{
        public fun afterVerified()
    }

    companion object{
        public fun getInstance() : JoinPhoneVerifyFragment{
            return JoinPhoneVerifyFragment()
        }
    }

    private var callbacks : CallBacks? = null
    private lateinit var binding : FragmentJoinPhoneVerifyBinding
    private val viewModel : JoinPhoneVerifyViewModel by lazy {
        ViewModelProvider(this).get(JoinPhoneVerifyViewModel::class.java)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as CallBacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_join_phone_verify, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
        binding.btnNext.setOnClickListener { viewModel.verify() }
        viewModel.verificationResult.observe(viewLifecycleOwner, Observer {
            if(it==true){   //인증 성공
                callbacks?.afterVerified()
            }else{  //인증 실패
                Toast.makeText(requireContext(),"인증번호를 다시 확인하세요.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setUI(){
        binding.editCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.code.value = p0.toString()
                if(p0?.length==0){
                    binding.btnNext.setEnabled(false)
                    binding.btnNext.setClickable(false)
                    binding.btnNext.setBackgroundResource(R.drawable.btn_radius_gray)
                }else{
                    binding.btnNext.setEnabled(true)
                    binding.btnNext.setClickable(true)
                    binding.btnNext.setBackgroundResource(R.drawable.btn_radius_main)
                }
            }


        })
    }


    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }





}