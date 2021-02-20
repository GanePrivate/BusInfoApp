package com.example.aitbusinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.tabs.TabLayout
import java.time.LocalTime

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TabLayoutの取得
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)

        // OnTabSelectedListenerの実装
        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {

            // タブが選択された際に呼ばれる
            override fun onTabSelected(tab: TabLayout.Tab) {
                Toast.makeText(this@MainActivity, tab.text, Toast.LENGTH_SHORT).show()

                val todayInfo = getTodayTimetable()     // 今日の運行時刻表を取得(バスが無い日はnull, null, 0, 0が返ってくる)
                val hour: Int = LocalTime.now().minute  // 現在の時間(hour)を取得

                //　大学行きの次のバスの出発時間を調べる(-1が帰ってきた場合はその時間のバスはもう無い)
                val toDaigakuInfo = getNextMin(todayInfo.toDaigaku, hour)
                var resultHour1_1 = hour

                //　八草行きの次のバスの出発時間を調べる(-1が帰ってきた場合はその時間のバスはもう無い)
                val toYakusaInfo = getNextMin(todayInfo.toYakusa, hour);
                var resultHour2_1 = hour

                // 次の次のバスの出発時刻を調べる(大学行き)
                var toDaigakuAfterNext = getAfterNextMin(toDaigakuInfo.third, todayInfo.toDaigaku, hour, resultHour1_1)

                // 次の次のバスの出発時刻を調べる(八草行き)
                var toYakusaAfterNext = getAfterNextMin(toYakusaInfo.third, todayInfo.toYakusa, hour, resultHour2_1)

                val messageView: TextView = findViewById(R.id.info)
                messageView.text = "${toDaigakuInfo}\n"

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
