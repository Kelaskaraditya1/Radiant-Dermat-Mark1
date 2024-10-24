package com.starkindustries.radientdermat.Frontend.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.transition.Visibility
import com.starkindustries.radientdermat.R
import com.starkindustries.radientdermat.databinding.ActivityMainBinding
class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        binding=DataBindingUtil.setContentView(this, R.layout.activity_main)
        LongOperation().execute()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    open inner class LongOperation: AsyncTask<String?, Void?, String?>()
    {
        override fun doInBackground(vararg params: String?): String? {
            for(i in 0..2)
            {
                try
                {
                    Thread.sleep(1000)
                }
                catch (e:Exception)
                {
                    Thread.interrupted()
                }
            }
            return "results"
        }
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val intent = Intent(this@MainActivity,LoginActivity::class.java)
            Handler(Looper.getMainLooper()).postDelayed({
               binding.progressBar.visibility=ProgressBar.INVISIBLE
            },4000)
            startActivity(intent)

            finish()
        }
    }
}