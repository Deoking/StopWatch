package com.iamdeok.stopwatch

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {

    //시간저장 변수
    private var time = 0
    //타이머
    private var timerTask: Timer? = null
    //상태 flag
    private var isRunning = false

    private var lap = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            //현재상태 플래그
            isRunning = !isRunning

            //실행중이 아니면 타이머 스타트
            if(isRunning) {
                start()
            }else {
                pause()
            }
        }

        lapButton.setOnClickListener {
            recordLapTime()
        }

        resetFab.setOnClickListener {
            reset()
        }

    }

    //타이머 스타트
    private fun start() {
        //플레이버튼은 일시정지 버튼으로 변경
        fab.setImageResource(R.drawable.ic_pause_black_24dp)

        //0.01초마다 time 증가
        timerTask = timer(period = 10){
            time++
            val sec = time / 100
            val milli = time % 100

            //timer는 워커스레드에서 동작하지만 UI를 조작해야하기 때문에 runOnUiThread 함수로 실행
            runOnUiThread {
                secTextView.text = "$sec"
                milliTextView.text = "$milli"
            }
        }
    }

    //타이머 정지
    private fun pause() {
        //일시정지 버튼을 플레이버튼으로
        fab.setImageResource(R.drawable.ic_play_arrow_black_24dp)
        //timerTask가 null아 아니면 cancel
        timerTask?.cancel()
    }

    //랩타임 기록
    private fun recordLapTime() {
        //ScrollLayout에 추가할 textview생성
        val lapTime = this.time
        val textView = TextView(this)
        textView.text = "Lap : $lap , Time : ${lapTime / 100}.${lapTime % 100}"

        //ScrollLayout에 textview추가
        lapLayout.addView(textView)
        lap++
    }

    private fun reset() {
        //timer초기화
        timerTask?.cancel()

        //변수초기화
        time = 0
        lap = 1
        isRunning = false

        //scrolllayout 비우기
        lapLayout.removeAllViews()

        fab.setImageResource(R.drawable.ic_play_arrow_black_24dp)
        secTextView.text = "0"
        milliTextView.text = "00"
    }
}
