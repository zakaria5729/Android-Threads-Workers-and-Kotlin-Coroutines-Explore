package com.example.androidconcurrency2020

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.androidconcurrency2020.databinding.ActivityMainBinding
import kotlin.concurrent.thread
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var imageViews: Array<ImageView>

    private val drawables = arrayOf(
        R.drawable.die_1, R.drawable.die_2,
        R.drawable.die_3, R.drawable.die_4,
        R.drawable.die_5, R.drawable.die_6
    )

    private val handler = object: Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            val bundle = msg.data
            val dieIndex = bundle?.getInt(DIE_INDEX_KEY) ?: 0
            val dieValue = bundle?.getInt(DIE_VALUE_KEY) ?: 1

            Log.i(LOG_TAG, "index = $dieIndex, value = $dieValue")
            imageViews[dieIndex].setImageResource(drawables[dieValue])
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding for view object references
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageViews = arrayOf(binding.die1, binding.die2, binding.die3, binding.die4, binding.die5)
        binding.rollButton.setOnClickListener { rollTheDice() }
    }

    private fun rollTheDice() {
        for (dieIndex in imageViews.indices) {
            thread(start = true) {
                Thread.sleep(dieIndex * 10L)

                val bundle = Bundle()
                bundle.putInt(DIE_INDEX_KEY, dieIndex)

                for (i in 1..10) {
                    bundle.putInt(DIE_VALUE_KEY, getDieValue())

                    Message().also {
                        it.data = bundle
                        handler.sendMessage(it)
                    }

                    Thread.sleep(150)
                }
            }
        }
    }

    private fun getDieValue(): Int {
        return Random.nextInt(1, 6)
    }
}
