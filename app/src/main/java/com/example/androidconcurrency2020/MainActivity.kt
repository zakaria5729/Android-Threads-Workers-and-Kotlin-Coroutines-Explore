package com.example.androidconcurrency2020

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import com.example.androidconcurrency2020.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.net.URL
import java.nio.charset.Charset

const val fileUrl = "https://2833069.youcanlearnit.net/lorem_ipsum.txt"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding for view object references
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize button click handlers
        with(binding) {
            runButton.setOnClickListener { runCode() }
            clearButton.setOnClickListener { clearOutput() }
        }

    }

    /**
     * Run some code
     */
    private fun runCode() {
        CoroutineScope(Dispatchers.Main).launch {
            val result = fetchSomething()
            log(result ?: "Null")
        }
    }

    /**
     * Clear log display
     */
    private fun clearOutput() {
        binding.logDisplay.text = ""
        scrollTextToEnd()
    }

    /**
     * Log output to logcat and the screen
     */
    @Suppress("SameParameterValue")
    private fun log(message: String) {
        Log.i(LOG_TAG, message)
        binding.logDisplay.append(message + "\n")
        scrollTextToEnd()
    }

    /**
     * Scroll to end. Wrapped in post() function so it's the last thing to happen
     */
    private fun scrollTextToEnd() {
        Handler().post { binding.scrollView.fullScroll(ScrollView.FOCUS_DOWN) }
    }

    private suspend fun fetchSomething(): String? {
//        delay(2000)
        log("Starting the request")
        return withContext(Dispatchers.IO) {
            val url = URL(fileUrl)
            return@withContext url.readText(Charset.defaultCharset())
        }
    }

}

