package com.example.meowdoroapp

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    //Variable music
    private var sound:MediaPlayer?=null
    private lateinit var play:ImageView
    private lateinit var stop:ImageView
    private val sounds = arrayOf(R.raw.forest, R.raw.rain, R.raw.ocean)
    private var musicIndex = 0

    //Variable's of timer
    private lateinit var timer: CountDownTimer
    private lateinit var seekBar: SeekBar
    private lateinit var timerTextView: TextView
    private lateinit var startPauseButton: ImageView
    private lateinit var restartButton: ImageView
    private var timeLeftInMillis: Long = 0
    private var isTimerRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Music
        play = findViewById(R.id.sound)
        stop = findViewById(R.id.stopSound)


        play.setOnClickListener {
            playSound()
        }
        stop.setOnClickListener {
            stopSound()
        }

        //Timer
        seekBar = findViewById(R.id.seekBar)
        timerTextView = findViewById(R.id.timerTextView)
        startPauseButton = findViewById(R.id.startPauseButton)
        restartButton = findViewById(R.id.restartButton)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (seekBar?.progress == 0){
                    startPauseButton.visibility = View.INVISIBLE
                }else{
                    startPauseButton.visibility = View.VISIBLE
                }
                timeLeftInMillis = progress * 1000L
                updateCountdownText()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

    }

    //Sound function's
    fun playSound() {
        if (sound == null) {
            sound = MediaPlayer.create(this, sounds[musicIndex])
            sound!!.isLooping = true
            sound!!.start()
            play.visibility = View.INVISIBLE
            stop.visibility = View.VISIBLE
        } else {
            sound?.start()
        }
        musicIndex = (musicIndex + 1) % sounds.size
        sound!!.setOnCompletionListener {
            sound!!.release()
            sound = null
            playSound()
        }
    }

    fun stopSound(){
        if(sound!=null){
            sound?.stop()
            sound?.release()
            sound?.release()
            sound = null
            play.visibility = View.VISIBLE
            stop.visibility = View.INVISIBLE
        }
    }

    override  fun onStop(){
        super.onStop()
        if (sound!=null){
            sound!!.release()
            sound=null
        }
    }

    //Timer function's
    fun startPauseButtonClicked(view: View) {
        if (isTimerRunning) {
            pauseTimer()
        } else {
            startTimer()
            startPauseButton.visibility = View.INVISIBLE
            restartButton.visibility = View.VISIBLE
            seekBar.visibility = View.INVISIBLE
        }
    }

    fun restartButtonClicked(view: View) {
        resetTimer()
        restartButton.visibility = View.INVISIBLE
        seekBar.visibility = View.VISIBLE
    }

    private fun startTimer() {
        timer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateCountdownText()
            }

            override fun onFinish() {
                isTimerRunning = false
                startPauseButton.setImageResource(R.drawable.play)
                resetTimer()
            }
        }.start()

        isTimerRunning = true
    }

    private fun pauseTimer() {
        timer.cancel()
        isTimerRunning = false
        startPauseButton.setImageResource(R.drawable.play)
    }

    private fun resetTimer() {
        timer.cancel()
        isTimerRunning = false
        timeLeftInMillis = 0
        seekBar.progress = 0
        updateCountdownText()
        startPauseButton.setImageResource(R.drawable.play)
    }

    private fun updateCountdownText() {
        val hours = (timeLeftInMillis / 500) / 3600
        val minutes = ((timeLeftInMillis / 500) % 3600) / 60
        val seconds = (timeLeftInMillis / 500) % 60

        val timeLeftFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds)
        timerTextView.text = timeLeftFormatted
    }
}