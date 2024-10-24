package com.starkindustries.radientdermat.Frontend.Fragment

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity.INPUT_METHOD_SERVICE
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import com.google.ai.client.generativeai.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.android.material.textfield.TextInputEditText
import com.starkindustries.radientdermat.R
import com.starkindustries.radientdermat.databinding.ActivityDashBoardBinding
import kotlinx.coroutines.runBlocking

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChatBotFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatBotFragment : Fragment() {
    lateinit var binding: ActivityDashBoardBinding
    lateinit var prompt:TextInputEditText
    lateinit var submitButton:AppCompatButton
    lateinit var responseText: AppCompatTextView
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chat_bot, container, false)
        prompt=view.findViewById(R.id.promptText)
        submitButton=view.findViewById(R.id.submitButton)
        responseText=view.findViewById(R.id.responseText)
        val generativeModel =
            GenerativeModel(
                // Specify a Gemini model appropriate for your use case
                modelName = "gemini-1.5-flash",
                // Access your API key as a Build Configuration variable (see "Set up your API key" above)
                apiKey =com.starkindustries.radientdermat.BuildConfig.API_KEY)
        submitButton.setOnClickListener(){
            if(!responseText.text.isEmpty())
                responseText.text=""

            runBlocking {
                val view = activity?.currentFocus
                if(view!=null) {
                    val manager: InputMethodManager = context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    manager.hideSoftInputFromWindow(view.windowToken,0)
                }
                if(prompt.text.toString().isEmpty()){
                    responseText.text="Please enter valid prompt"
                    return@runBlocking
                }
                else{
                    val response = generativeModel.generateContent(prompt.text.toString().trim())
                    responseText.text=response.text.toString().trim()
                }
                 }
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChatBotFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChatBotFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle back press
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Navigate to the new fragment on back press
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.framelayout,HomeFragment())  // Replace with your desired fragment
            transaction.commit()
        }
    }


}