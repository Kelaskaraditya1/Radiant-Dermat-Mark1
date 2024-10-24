package com.starkindustries.radientdermat.Frontend.Activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.starkindustries.radientdermat.Frontend.Fragment.ChatBotFragment
import com.starkindustries.radientdermat.Frontend.Fragment.HomeFragment
import com.starkindustries.radientdermat.Keys.Keys
import com.starkindustries.radientdermat.R
import com.starkindustries.radientdermat.databinding.ActivityDashBoardBinding

class DashBoardActivity : AppCompatActivity() {
    lateinit var binding: ActivityDashBoardBinding
    lateinit var auth: FirebaseAuth
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dash_board)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dash_board)
        auth = FirebaseAuth.getInstance()
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setTitle("Radiant Dermat")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Load HomeFragment initially
        val manager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = manager.beginTransaction()
        transaction.add(R.id.framelayout, HomeFragment())
        transaction.commit()

        // Chat button click listener
//        binding.chatButton.setOnClickListener {
//            binding.chatButton.visibility = View.GONE
//            val manager: FragmentManager = supportFragmentManager
//            val transaction: FragmentTransaction = manager.beginTransaction()
//            transaction.replace(R.id.framelayout, ChatBotFragment())
//            transaction.addToBackStack(null)  // Add to back stack for back navigation
//            transaction.commit()
//        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }



    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()  // Default back press behavior
        }
    }
}
