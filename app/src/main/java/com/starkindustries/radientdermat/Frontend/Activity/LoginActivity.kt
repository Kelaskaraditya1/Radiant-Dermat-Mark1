package com.starkindustries.radientdermat.Frontend.Activity
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.starkindustries.radientdermat.Keys.Keys
import com.starkindustries.radientdermat.R
import com.starkindustries.radientdermat.databinding.ActivityLoginBinding
class LoginActivity : AppCompatActivity() {
    lateinit var binding:ActivityLoginBinding
    var passed=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_login)
        binding.registerButton.setOnClickListener(){
            val intent= Intent(this,RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.loginPasswordText.setOnTouchListener(object: View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event != null) {
                    if(event.action== MotionEvent.ACTION_UP) {
                        val selection=binding.loginPasswordText.selectionEnd
                        if(event.rawX>(binding.loginPasswordText.right-binding.loginPasswordText.compoundDrawables[Keys.RIGHT].bounds.width())) {
                            if(passed) {
                                binding.loginPasswordText.transformationMethod=
                                    PasswordTransformationMethod.getInstance()
                                binding.loginPasswordText.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.visibility_off,0)
                                passed=false
                            } else {
                                binding.loginPasswordText.transformationMethod=
                                    HideReturnsTransformationMethod.getInstance()
                                binding.loginPasswordText.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.visibility_on,0)
                                passed=true
                            }
                            binding.loginPasswordText.setSelection(selection)
                            return true
                        }
                    }
                }
                return false
            }
        })
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}