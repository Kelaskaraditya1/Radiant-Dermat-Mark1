package com.starkindustries.radientdermat.Frontend.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.starkindustries.radientdermat.Keys.Keys
import com.starkindustries.radientdermat.R
import com.starkindustries.radientdermat.databinding.ActivityLoginBinding
class LoginActivity : AppCompatActivity() {
    lateinit var binding:ActivityLoginBinding
    var passed=false
    lateinit var firebaseFirestore: FirebaseFirestore
    lateinit var docRefrence: DocumentReference
    lateinit var auth: FirebaseAuth
    lateinit var user: FirebaseUser
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
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
        auth=FirebaseAuth.getInstance()
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
        binding.loginButton.setOnClickListener()
        {
            if(TextUtils.isEmpty(binding.loginUsernameText.text.toString().trim()))
            {
                binding.loginUsernameText.setError("Field is Empty!!")
                return@setOnClickListener
            }
            else if(TextUtils.isEmpty(binding.loginPasswordText.text.toString().trim()))
            {
                binding.loginPasswordText.setError("Field is Empty!!")
                return@setOnClickListener
            }
            else if(binding.loginPasswordText.text.toString().trim().length<8)
            {
                binding.loginPasswordText.setError("Password should be atleast of 8 charecters.")
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(binding.loginUsernameText.text.toString().trim(),binding.loginPasswordText.text.toString().trim())
                .addOnCompleteListener()
                {
                    if(it.isSuccessful)
                    {
                        val view = this.currentFocus
                        if(view!=null)
                        {
                            val manager: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                            manager.hideSoftInputFromWindow(view.windowToken,0)
                        }
                        sharedPreferences=getSharedPreferences(Keys.SHARED_PREFRENCES_NAME, MODE_PRIVATE)
                        editor=sharedPreferences.edit()
                        editor.putBoolean(Keys.LOGIN_STATUS,true)
                        editor.apply()
                        user =auth.currentUser!!
                        val intent = Intent(this,DashBoardActivity::class.java)
                        startActivity(intent)
                        finish()
                        Toast.makeText(applicationContext, "Salam "+user.displayName.toString().trim()+" bhai!!", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener()
                {
                    Toast.makeText(applicationContext, "Check your Internet connection!!", Toast.LENGTH_SHORT).show()
                    Toast.makeText(applicationContext, "Either Email or Password is incorrect.", Toast.LENGTH_SHORT).show()
                }
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}