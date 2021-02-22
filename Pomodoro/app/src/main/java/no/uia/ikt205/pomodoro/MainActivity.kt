package no.uia.ikt205.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import no.uia.ikt205.pomodoro.util.intOrString
import no.uia.ikt205.pomodoro.util.millisecondsToDescriptiveTime
import no.uia.ikt205.pomodoro.util.minutesToMilliSeconds

open class SeekBarListener : SeekBar.OnSeekBarChangeListener {

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }
}

class MainActivity : AppCompatActivity()
{
    lateinit var timer:CountDownTimer
    lateinit var countdownDisplay:TextView
    lateinit var startButton:Button
    lateinit var seekBarWorkTimer:SeekBar
    lateinit var seekBarBreakTimer:SeekBar
    lateinit var editTextIntervalDisplay:EditText

    var numberOfIntervals:Int = 0
    val timeTicks:Long = 1000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startButton = findViewById<Button>(R.id.startCountdownButton)
        startButton.setOnClickListener(){
            numberOfIntervals = getNumberOfIntervalsAsInt(editTextIntervalDisplay.text.toString())
            when(numberOfIntervals){
                -1 -> Toast.makeText(this@MainActivity,"input has to be a number", Toast.LENGTH_SHORT).show()
                else -> startWorkTimer(it, seekBarWorkTimer.progress.toLong())
            }
        }
        countdownDisplay = findViewById<TextView>(R.id.countDownView)

        seekBarWorkTimer = findViewById<SeekBar>(R.id.seekBarTimer)
        seekBarWorkTimer.setOnSeekBarChangeListener(object : SeekBarListener() {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(seekBar != null){
                    updateCountDownDisplay(minutesToMilliSeconds(progress.toLong()))
                }
            }
        })

        seekBarBreakTimer = findViewById<SeekBar>(R.id.seekBarPauseTimer)
        seekBarBreakTimer.setOnSeekBarChangeListener(object : SeekBarListener() {

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (seekBar != null) {
                    Toast.makeText(this@MainActivity,"selected break time ${seekBar.progress} minutes", Toast.LENGTH_SHORT).show()
                }
            }
        })
        editTextIntervalDisplay = findViewById<EditText>(R.id.editTextIntervalDisplay)
    }

    fun startWorkTimer(v:View, workTime:Long){
        disableWidgets()
        timer = object : CountDownTimer(minutesToMilliSeconds(workTime), timeTicks){
            override fun onFinish() {
                startBreakTimer(v, seekBarBreakTimer.progress.toLong())
            }

            override fun onTick(millisUntilFinished: Long) {
                updateCountDownDisplay(millisUntilFinished)
            }
        }
        timer.start()
    }

    fun startBreakTimer(v:View, breakTime:Long){
        timer = object : CountDownTimer(minutesToMilliSeconds(breakTime), timeTicks){
            override fun onFinish() {
                updateEditTextDisplay()
                if(numberOfIntervals == 0){
                    Toast.makeText(this@MainActivity,"Finished", Toast.LENGTH_SHORT).show()
                    enableWidgets()
                }
                else{
                    startWorkTimer(v, seekBarWorkTimer.progress.toLong())
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                updateCountDownDisplay(millisUntilFinished)
            }
        }
        Toast.makeText(this@MainActivity,"Break", Toast.LENGTH_LONG).show()
        timer.start()
    }

    fun updateEditTextDisplay(){
        numberOfIntervals--
        editTextIntervalDisplay.text.clear()
        editTextIntervalDisplay.text.insert(0, numberOfIntervals.toString())
    }

    fun disableWidgets(){
        startButton.isEnabled = false
        seekBarWorkTimer.isEnabled = false
        seekBarBreakTimer.isEnabled = false
        editTextIntervalDisplay.isEnabled = false
    }

    fun enableWidgets(){
        startButton.isEnabled = true
        seekBarWorkTimer.isEnabled = true
        seekBarBreakTimer.isEnabled = true
        editTextIntervalDisplay.isEnabled = true
    }

    fun getNumberOfIntervalsAsInt(editTextString:String):Int{
        return editTextString.intOrString()
    }

    fun updateCountDownDisplay(timeInMs:Long){
        countdownDisplay.text = millisecondsToDescriptiveTime(timeInMs)
    }

}