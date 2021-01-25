package no.uia.ikt205.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import no.uia.ikt205.pomodoro.util.millisecondsToDescriptiveTime

class MainActivity : AppCompatActivity() {

    lateinit var timer:CountDownTimer
    lateinit var startButton:Button
    lateinit var countdownDisplay:TextView
    lateinit var timerButton30:Button
    lateinit var timerButton60:Button
    lateinit var timerButton90:Button
    lateinit var timerButton120:Button

    var timeToCountDownInMs:Long = 0L
    var isActive:Boolean = false

    val timeTicks:Long = 1000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       startButton = findViewById<Button>(R.id.startCountdownButton)
       startButton.setOnClickListener(){
           if(!isActive) startCountDown(it, timeToCountDownInMs)
       }

        timerButton30 = findViewById<Button>(R.id.timerButton30)
        timerButton30.setOnClickListener(){
            if(!isActive){
                timeToCountDownInMs = 1800000L
                updateSelectedTime(timeToCountDownInMs)
            }
        }

        timerButton60 = findViewById<Button>(R.id.timerButton60)
        timerButton60.setOnClickListener(){
            if(!isActive){
                timeToCountDownInMs = 3600000L
                updateSelectedTime(timeToCountDownInMs)
            }
        }

        timerButton90 = findViewById<Button>(R.id.timerButton90)
        timerButton90.setOnClickListener(){
            if(!isActive){
                timeToCountDownInMs = 5400000L
                updateSelectedTime(timeToCountDownInMs)
            }
        }

        timerButton120 = findViewById<Button>(R.id.timerButton120)
        timerButton120.setOnClickListener(){
            if(!isActive){
                timeToCountDownInMs = 7200000L
                updateSelectedTime(timeToCountDownInMs)
            }
        }

       countdownDisplay = findViewById<TextView>(R.id.countDownView)
    }

    fun startCountDown(v: View, timeInMs: Long){
        isActive = true
        timer = object : CountDownTimer(timeInMs, timeTicks) {
            override fun onFinish() {
                Toast.makeText(this@MainActivity,"Arbeidsøkt er ferdig", Toast.LENGTH_SHORT).show()
                isActive = false
            }

            override fun onTick(millisUntilFinished: Long) {
               updateCountDownDisplay(millisUntilFinished)
            }
        }

        timer.start()

    }

    fun updateCountDownDisplay(timeInMs:Long){
        countdownDisplay.text = millisecondsToDescriptiveTime(timeInMs)
    }

    fun updateSelectedTime(timeInMs: Long){
        countdownDisplay.text = millisecondsToDescriptiveTime(timeInMs)
    }
}