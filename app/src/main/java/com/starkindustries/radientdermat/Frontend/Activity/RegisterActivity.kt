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
import com.starkindustries.radientdermat.databinding.ActivityRegisterBinding
class RegisterActivity : AppCompatActivity() {
    lateinit var binding:ActivityRegisterBinding
    var passed=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_register)
        binding.loginButton.setOnClickListener(){
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.registerPasswordText.setOnTouchListener(object: View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event != null) {
                    if(event.action== MotionEvent.ACTION_UP) {
                        val selection=binding.registerPasswordText.selectionEnd
                        if(event.rawX>(binding.registerPasswordText.right-binding.registerPasswordText.compoundDrawables[Keys.RIGHT].bounds.width())) {
                            if(passed) {
                                binding.registerPasswordText.transformationMethod=
                                    PasswordTransformationMethod.getInstance()
                                binding.registerPasswordText.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.visibility_off,0)
                                passed=false
                            } else {
                                binding.registerPasswordText.transformationMethod=
                                    HideReturnsTransformationMethod.getInstance()
                                binding.registerPasswordText.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.visibility_on,0)
                                passed=true
                            }
                            binding.registerPasswordText.setSelection(selection)
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