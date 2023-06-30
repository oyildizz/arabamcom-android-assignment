package com.example.kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.kotlin.databinding.ActivityMainBinding

  class MainActivity : AppCompatActivity() {
   private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener{
            if (binding.textInputEditText.text!!.isNotEmpty()){
                var carName= binding.textInputEditText.text.toString()
                when(carName){
                    "hh"->binding.textView.text="OPEL ASTRA"
                }
            }
            else{
                binding.textView.text ="Herhangi bir araba modeli giriniz."
            }
        }


    }
}