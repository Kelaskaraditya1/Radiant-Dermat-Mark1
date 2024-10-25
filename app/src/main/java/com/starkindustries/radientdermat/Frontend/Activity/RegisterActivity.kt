package com.starkindustries.radientdermat.Frontend.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.starkindustries.radientdermat.Keys.Keys
import com.starkindustries.radientdermat.R
import com.starkindustries.radientdermat.databinding.ActivityRegisterBinding
class RegisterActivity : AppCompatActivity() {
    lateinit var binding:ActivityRegisterBinding
    var passed=false
    lateinit var auth:FirebaseAuth
    lateinit var user: FirebaseUser
    lateinit var firestore: FirebaseFirestore
    lateinit var docrefrence: DocumentReference
    lateinit var storageRefrence: StorageReference
    lateinit var childRefrence: StorageReference
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_register)
        auth=FirebaseAuth.getInstance()
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
        binding.profileImageButton.setOnClickListener(){
            val intent = Intent(Intent.ACTION_PICK)
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, Keys.GALLERY_REQ_CODE)
        }
        binding.signupButton.setOnClickListener()
        {
            if(TextUtils.isEmpty(binding.registerNameText.text.toString().trim()))
            {
                binding.registerNameText.setError("Field is Empty!!")
                return@setOnClickListener
            }
            else if(TextUtils.isEmpty(binding.registerEmailText.text.toString().trim()))
            {
                binding.registerEmailText.setError("Field is Empty!!")
                return@setOnClickListener
            }
            else if(TextUtils.isEmpty(binding.registerPhoneNoText.text.toString().trim()))
            {
                binding.registerPhoneNoText.setError("Field is Empty!!")
                return@setOnClickListener
            }
            else if(binding.registerPhoneNoText.text.toString().trim().length<10)
            {
                binding.registerPhoneNoText.setError("Phone no should be at least of 10 digits")
                return@setOnClickListener
            }
            else if(TextUtils.isEmpty(binding.registerUsernameText.text.toString().trim()))
            {
                binding.registerUsernameText.setError("Field is Empty!!")
                return@setOnClickListener
            }
            else if(TextUtils.isEmpty(binding.registerPasswordText.text.toString().trim()))
            {
                binding.registerPasswordText.setError("Field is Empty!!")
                return@setOnClickListener
            }
            else if(binding.registerPasswordText.text.toString().trim().length<8)
            {
                binding.registerPasswordText.setError("Password should be atleast of 8 charecters.")
                return@setOnClickListener
            }
            auth.createUserWithEmailAndPassword(binding.registerEmailText.text.toString().trim(),binding.registerPasswordText.text.toString().trim())
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
                        user=auth.currentUser!!
                        storageRefrence= FirebaseStorage.getInstance().reference
                        childRefrence=storageRefrence.child(binding.registerNameText.text.toString().trim()+"/"+user.uid+"/"+Keys.IMAGES+"/"+Keys.USER_PROFILE_IMAGE)
                        childRefrence.putFile(binding.profileImage.getTag() as Uri).addOnSuccessListener()
                        {
                            it.storage.downloadUrl.addOnSuccessListener {
                                    uri->
                                firestore=FirebaseFirestore.getInstance()
                                docrefrence=firestore.collection(Keys.COLLECTION_NAME).document(user.uid)
                                val map = mutableMapOf<String,Any>()
                                map.put(Keys.NAME,binding.registerNameText.text.toString().trim())
                                map.put(Keys.EMAIL,binding.registerEmailText.text.toString().trim())
                                map.put(Keys.PHONE_NO,binding.registerPhoneNoText.text.toString().trim())
                                map.put(Keys.DOWNLOAD_URL,uri.toString().trim())
                                map.put(Keys.USERNAME,binding.registerUsernameText.text.toString().trim())
                                map.put(Keys.PASSWORD,binding.registerPasswordText.text.toString().trim())
                                docrefrence.set(map).addOnCompleteListener()
                                {
                                    if(it.isSuccessful)
                                    {
                                        val update = UserProfileChangeRequest.Builder()
                                            .setDisplayName(binding.registerUsernameText.text.toString().trim())
                                            .setPhotoUri(uri)
                                            .build()
                                        user.updateProfile(update).addOnCompleteListener()
                                        {
                                            if(it.isSuccessful)
                                            {
                                                sharedPreferences=getSharedPreferences(Keys.SHARED_PREFRENCES_NAME, MODE_PRIVATE)
                                                editor=sharedPreferences.edit()
                                                editor.putBoolean(Keys.LOGIN_STATUS,true)
                                                editor.apply()
                                                val intent = Intent(this,DashBoardActivity::class.java)
                                                editor.putString(Keys.USERNAME,binding.registerUsernameText.text.toString())
                                                editor.apply()
                                                startActivity(intent)
                                                finish()
                                            }
                                        }.addOnFailureListener()
                                        {
                                            Log.d("ErrorListner"," "+it.message.toString().trim())
                                        }
                                    }
                                }.addOnFailureListener {
                                    Log.d("ErrorListner"," "+it.message.toString().trim())
                                }
                            }.addOnFailureListener {
                                Log.d("ErrorListner"," "+it.message.toString().trim())
                            }
                        }.addOnFailureListener()
                        {
                            Log.d("ErrorListner"," "+it.message.toString().trim())
                        }
                    }
                }.addOnFailureListener()
                {
                    Log.d("ErrorListner"," "+it.message.toString().trim())
                }
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== RESULT_OK)
        {
            if(requestCode==Keys.GALLERY_REQ_CODE)
            {
                binding.profileImage.setImageURI(data?.data!!)
                binding.profileImage.setTag(data?.data)
            }
        }
    }
}