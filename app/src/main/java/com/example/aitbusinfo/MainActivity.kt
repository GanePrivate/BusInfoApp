package com.example.aitbusinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.tabs.TabLayout
import java.time.LocalTime
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TabLayoutの取得
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)

        // ハンドラの定義 (サブスレッドからメインスレッドへメッセージを受け渡すためのクラス)
        val hnd = Handler()

        // ハンドラに渡す関数 (Runnable は、呼び出されたときにスレッド内で実行される run() メソッドを持つ、単一抽象メソッド（SAM）インターフェース)
        val rnb = object : Runnable{
            override fun run() {
                // 今日のバスの運行情報を取得
                val busInfo = getAllData()

                // Log.w("tab", tabLayout.selectedTabPosition.toString())

                when (tabLayout.selectedTabPosition) {

                    // 大学→から八草のタブが選択されていた時
                    1 -> {
                        // 今日バスの運行があるか確認する
                        if (busInfo.todayInfo.daiya == null) {
                            nextTime.text = "本日、バスの運行は\nありません"
                            afterNextTime.visibility = View.INVISIBLE
                            daiya.visibility = View.INVISIBLE
                            label2.visibility = View.INVISIBLE
                            label3.visibility = View.INVISIBLE
                        } else {
                            // 次の出発時刻を表示する
                            nextTime.text = if (busInfo.toYakusaInfo.minutes != -1) "%02d:%02d".format(busInfo.toYakusaInfo.hour, busInfo.toYakusaInfo.minutes) else if (LocalTime.now().minute <= 7) "[始発] 08:%02d".format(busInfo.todayInfo.toYakusaFirst) else "本日の運行は終了しました"

                            // 次の次の出発時間を表示する
                            afterNextTime.text = if (busInfo.toYakusaAfterNext.minutes != -1) "%02d:%02d".format(busInfo.toYakusaAfterNext.hour, busInfo.toYakusaAfterNext.minutes) else "この時間の\nバスはありません"

                            // 今日の運行ダイヤを表示する
                            daiya.text = "今日は${busInfo.todayInfo.daiya}ダイヤです"
                        }
                    }

                    else -> {
                        // 今日バスの運行があるか確認する
                        if (busInfo.todayInfo.daiya == null) {
                            nextTime.text = "本日、バスの運行は\nありません"
                            afterNextTime.visibility = View.INVISIBLE
                            daiya.visibility = View.INVISIBLE
                            label2.visibility = View.INVISIBLE
                            label3.visibility = View.INVISIBLE
                        } else {
                            // 次の出発時刻を表示する
                            nextTime.text = if (busInfo.toDaigakuInfo.minutes != -1) "%02d:%02d".format(busInfo.toDaigakuInfo.hour, busInfo.toDaigakuInfo.minutes) else if (LocalTime.now().minute <= 7) "[始発] 08:%02d".format(busInfo.todayInfo.toDaigakuFirst) else "本日の運行は終了しました"

                            // 次の次の出発時間を表示する
                            afterNextTime.text = if (busInfo.toDaigakuAfterNext.minutes != -1) "%02d:%02d".format(busInfo.toDaigakuAfterNext.hour, busInfo.toDaigakuAfterNext.minutes) else "この時間の\nバスはありません"

                            // 今日の運行ダイヤを表示する
                            daiya.text = "今日は${busInfo.todayInfo.daiya}ダイヤです"
                        }
                    }
                }

                // 1秒毎に実行する
                hnd.postDelayed(this,100)
            }
        }

        // 実際にハンドラに渡す
        hnd.post(rnb)

        // OnTabSelectedListenerの実装
        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {

            // タブが選択された際に呼ばれる
            override fun onTabSelected(tab: TabLayout.Tab) {
                // トーストメッセージを出す
                Toast.makeText(this@MainActivity, tab.text, Toast.LENGTH_SHORT).show()
            }

            // タブが未選択になった際に呼ばれる
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            // 同じタブが選択された際に呼ばれる
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

}
