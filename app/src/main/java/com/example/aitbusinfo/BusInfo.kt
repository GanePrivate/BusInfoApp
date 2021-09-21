package com.example.aitbusinfo

import android.util.Log
import android.view.View
import android.widget.TextView
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


data class daiyaData(
        val daiya: String?,
        val toDaigaku: Map<Int, List<Int>?>,
        val toYakusa:  Map<Int, List<Int>?>,
        val toDaigakuFirst: Int,
        val toYakusaFirst: Int
)

data class NextInfo(
        val minutes: Int,
        val hour: Int,
        val flag: Boolean
)

data class AfterNextInfo(
        val minutes: Int,
        val hour: Int
)

data class ReturnData(
        val todayInfo: daiyaData,
        val toDaigakuInfo: NextInfo,
        val toYakusaInfo: NextInfo,
        val toDaigakuAfterNext: AfterNextInfo,
        val toYakusaAfterNext: AfterNextInfo
)


fun getNearVal(minutes: List<Int>?, mode: Int = 0, reset: Boolean = false): Int {
    /*
    最も近い値を返す関数
    mode = 0:通常の処理,  1:次の時間の処理,  3:次の次の時間の処理
    :param minutes:     # array:その時間の時刻表(分)の配列データ
    :param mode:        # int：動作モードを指定する値
    :param reset:       # bool：時間(分)をリセットするか
    :return:            # int：現在時刻から一番近い出発時間(分)
    */


    // 時刻表データ(分)が空なら0を入れておく
    val size: Int = if (minutes == null) 0 else minutes.size

    // オブジェクトのサイズを確認
    if (size < 1 || minutes == null) return -1

    // 今の時間(分)
    var now_minutes: Int = LocalTime.now().minute


    // for (var key in minutes){
    //     if (minutes.hasOwnProperty(key)){
    //         console.log(key, minutes[key]);
    //     }
    // }

    // 現在時間+1の時刻表データ(分)を返す処理
    if (mode == 1) return minutes[0]

    // リセットフラグを確認する
    if (reset) now_minutes = 0

    var l_flag: Boolean = false
    // 次の発車時間が見つかったらその時間を返す
    for (i in 0..size-1) {
        if (minutes[i] >= now_minutes && mode == 0) {
            return minutes[i]
        }

        // 次の次のバスの時刻を返す
        if (minutes[i] >= now_minutes && mode == 3 && l_flag) {
            return minutes[i]
        }

        if (minutes[i] >= now_minutes && mode == 3) {
            l_flag = true
        }
    }

    // 次の出発時間が見つからなかったら-1を返す
    return -1
}


fun getTodayTimetable(): daiyaData {
    /*
    今日の時刻表データを返す関数
    :return:    # array：toDaigaku  大学行きの今日の時刻表データ,
                # array：toYakusa   八草行きの今日の時刻表データ,
                # int：toDaigakuFirst  大学行きの始発時間(分),
                # int：toYakusaFirst   八草行きの始発時間(分)
    */

    // 今日の日付けを取得
    // val month: Int = LocalDate.now().dayOfMonth

    //　今日の日付をYYYY-MM-DDの形式で取得
    // val today = "2020-6-18"
    val today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    Log.w("今日の日付", today)

    // 時刻表データを定義(JSON形式)
    val A1Map = mapOf(0 to null, 1 to null, 2 to null, 3 to null, 4 to null, 5 to null, 6 to null, 7 to null, 8 to listOf(0,5,10,15,20,25,30,35,40,45,50,55), 9 to listOf(0,5,10,15,20,25,30,35,40,50,55), 10 to listOf(0,5,10,15,20,25,30,35,45,55), 11 to listOf(5,15,25,35,45,55), 12 to listOf(5,15,25,35,45,55), 13 to listOf(5,20,35,50), 14 to listOf(5,15,25,35,45,55), 15 to listOf(5,15,30,45), 16 to listOf(0,15,30,45), 17 to listOf(0,10,25,40), 18 to listOf(0,15,45), 19 to listOf(0,15,30,45), 20 to listOf(0,30), 21 to listOf(0), 22 to null, 23 to null)
    val A2Map = mapOf(0 to null, 1 to null, 2 to null, 3 to null, 4 to null, 5 to null, 6 to null, 7 to null, 8 to listOf(20,50), 9 to listOf(20,50), 10 to listOf(20,50), 11 to listOf(0,10,20,30,40,50), 12 to listOf(0,10,20,30,40,50), 13 to listOf(0,15,30,45), 14 to listOf(0,10,20,30,40,45,50,55), 15 to listOf(0,10,20,30,40,50), 16 to listOf(0,5,10,15,25,30,35,40,45,50,55), 17 to listOf(0,10,15,20,25,30,35,40,45,55), 18 to listOf(0,10,20,30,40,50), 19 to listOf(0,15,30,45), 20 to listOf(0,15,30,45), 21 to listOf(0,15,30,45), 22 to null, 23 to null)
    val B1Map = mapOf(0 to null, 1 to null, 2 to null, 3 to null, 4 to null, 5 to null, 6 to null, 7 to null, 8 to listOf(0,10,20,35,45), 9 to listOf(0,5,25,35,50), 10 to listOf(0,10,30,55), 11 to listOf(0,25,50), 12 to listOf(25), 13 to listOf(35), 14 to listOf(5,35), 15 to listOf(5,35), 16 to listOf(5,45), 17 to listOf(10,45), 18 to listOf(5,35), 19 to listOf(35), 20 to listOf(25), 21 to listOf(5), 22 to null, 23 to null)
    val B2Map = mapOf(0 to null, 1 to null, 2 to null, 3 to null, 4 to null, 5 to null, 6 to null, 7 to null, 8 to listOf(50), 9 to listOf(40), 10 to listOf(5,50), 11 to listOf(15,40), 12 to listOf(10), 13 to listOf(20,50), 14 to listOf(20,50), 15 to listOf(20,50), 16 to listOf(20), 17 to listOf(0,30,55), 18 to listOf(20,50), 19 to listOf(20,50), 20 to listOf(40), 21 to listOf(30), 22 to null, 23 to null)
    val C1Map = mapOf(0 to null, 1 to null, 2 to null, 3 to null, 4 to null, 5 to null, 6 to null, 7 to null, 8 to listOf(10,35), 9 to listOf(0,25,50), 10 to listOf(10,55), 11 to listOf(25,50), 12 to listOf(25), 13 to listOf(35), 14 to listOf(5,35), 15 to listOf(5,35), 16 to listOf(5,45), 17 to listOf(10,45), 18 to listOf(5,35), 19 to listOf(35), 20 to listOf(25), 21 to listOf(5), 22 to null, 23 to null)
    val C2Map = mapOf(0 to null, 1 to null, 2 to null, 3 to null, 4 to null, 5 to null, 6 to null, 7 to null, 8 to listOf(50), 9 to listOf(40), 10 to listOf(5,50), 11 to listOf(15,40), 12 to listOf(10), 13 to listOf(20,50), 14 to listOf(20,50), 15 to listOf(20,50), 16 to listOf(20), 17 to listOf(0,30,55), 18 to listOf(20,50), 19 to listOf(20,50), 20 to listOf(40), 21 to listOf(30), 22 to null, 23 to null)
    val daiyaMap = mapOf("2021-04-01" to "C", "2021-04-02" to "C", "2021-04-03" to null, "2021-04-04" to null, "2021-04-05" to "A", "2021-04-06" to "B", "2021-04-07" to "B", "2021-04-08" to "B", "2021-04-09" to "B", "2021-04-10" to null, "2021-04-11" to null, "2021-04-12" to "A", "2021-04-13" to "A", "2021-04-14" to "A", "2021-04-15" to "A", "2021-04-16" to "A", "2021-04-17" to null, "2021-04-18" to null, "2021-04-19" to "A", "2021-04-20" to "A", "2021-04-21" to "A", "2021-04-22" to "A", "2021-04-23" to "A", "2021-04-24" to null, "2021-04-25" to null, "2021-04-26" to "A", "2021-04-27" to "A", "2021-04-28" to "A", "2021-04-29" to "A", "2021-04-30" to "A", "2021-05-01" to null, "2021-05-02" to null, "2021-05-03" to null, "2021-05-04" to null, "2021-05-05" to null, "2021-05-06" to "A", "2021-05-07" to "A", "2021-05-08" to null, "2021-05-09" to null, "2021-05-10" to "A", "2021-05-11" to "A", "2021-05-12" to "A", "2021-05-13" to "A", "2021-05-14" to "A", "2021-05-15" to null, "2021-05-16" to null, "2021-05-17" to "A", "2021-05-18" to "A", "2021-05-19" to "A", "2021-05-20" to "A", "2021-05-21" to "A", "2021-05-22" to null, "2021-05-23" to null, "2021-05-24" to "A", "2021-05-25" to "A", "2021-05-26" to "A", "2021-05-27" to "A", "2021-05-28" to "A", "2021-05-29" to "A", "2021-05-30" to "A", "2021-05-31" to "A", "2021-06-01" to "A", "2021-06-02" to "A", "2021-06-03" to "A", "2021-06-04" to "A", "2021-06-05" to null, "2021-06-06" to null, "2021-06-07" to "A", "2021-06-08" to "A", "2021-06-09" to "A", "2021-06-10" to "A", "2021-06-11" to "A", "2021-06-12" to null, "2021-06-13" to null, "2021-06-14" to "A", "2021-06-15" to "A", "2021-06-16" to "A", "2021-06-17" to "A", "2021-06-18" to "A", "2021-06-19" to null, "2021-06-20" to null, "2021-06-21" to "A", "2021-06-22" to "A", "2021-06-23" to "A", "2021-06-24" to "A", "2021-06-25" to "A", "2021-06-26" to null, "2021-06-27" to null, "2021-06-28" to "A", "2021-06-29" to "A", "2021-06-30" to "A", "2021-07-01" to "A", "2021-07-02" to "A", "2021-07-03" to null, "2021-07-04" to null, "2021-07-05" to "A", "2021-07-06" to "A", "2021-07-07" to "A", "2021-07-08" to "A", "2021-07-09" to "A", "2021-07-10" to null, "2021-07-11" to null, "2021-07-12" to "A", "2021-07-13" to "A", "2021-07-14" to "A", "2021-07-15" to "A", "2021-07-16" to "A", "2021-07-17" to "A", "2021-07-18" to "A", "2021-07-19" to "A", "2021-07-20" to "A", "2021-07-21" to "A", "2021-07-22" to null, "2021-07-23" to null, "2021-07-24" to "A", "2021-07-25" to "A", "2021-07-26" to "A", "2021-07-27" to "A", "2021-07-28" to "A", "2021-07-29" to "A", "2021-07-30" to "A", "2021-07-31" to null, "2021-08-01" to null, "2021-08-02" to "A", "2021-08-03" to "A", "2021-08-04" to "A", "2021-08-05" to "A", "2021-08-06" to "A", "2021-08-07" to null, "2021-08-08" to null, "2021-08-09" to null, "2021-08-10" to "A", "2021-08-11" to "C", "2021-08-12" to "C", "2021-08-13" to "C", "2021-08-14" to null, "2021-08-15" to null, "2021-08-16" to null, "2021-08-17" to "C", "2021-08-18" to "B", "2021-08-19" to "B", "2021-08-20" to "B", "2021-08-21" to null, "2021-08-22" to null, "2021-08-23" to "C", "2021-08-24" to "C", "2021-08-25" to "C", "2021-08-26" to "C", "2021-08-27" to "C", "2021-08-28" to null, "2021-08-29" to null, "2021-08-30" to "C", "2021-08-31" to "C", "2021-09-1" to "C", "2021-09-2" to "C", "2021-09-3" to "C", "2021-09-4" to null, "2021-09-5" to null, "2021-09-6" to "C", "2021-09-7" to "C", "2021-09-8" to "C", "2021-09-9" to "C", "2021-09-10" to "C", "2021-09-11" to null, "2021-09-12" to null, "2021-09-13" to "C", "2021-09-14" to "C", "2021-09-15" to "C", "2021-09-16" to "C", "2021-09-17" to "C", "2021-09-18" to null, "2021-09-19" to null, "2021-09-20" to null, "2021-09-21" to "B", "2021-09-22" to "A", "2021-09-23" to null, "2021-09-24" to "B", "2021-09-25" to null, "2021-09-26" to null, "2021-09-27" to "A", "2021-09-28" to "A", "2021-09-29" to "A", "2021-09-30" to "A", "2021-10-01" to "A", "2021-10-02" to null, "2021-10-03" to null, "2021-10-04" to "A", "2021-10-05" to "A", "2021-10-06" to "A", "2021-10-07" to "B", "2021-10-08" to "B", "2021-10-09" to "A", "2021-10-10" to "A", "2021-10-11" to null, "2021-10-12" to "A", "2021-10-13" to "A", "2021-10-14" to "A", "2021-10-15" to "A", "2021-10-16" to "C", "2021-10-17" to null, "2021-10-18" to "A", "2021-10-19" to "A", "2021-10-20" to "A", "2021-10-21" to "A", "2021-10-22" to "A", "2021-10-23" to "C", "2021-10-24" to null, "2021-10-25" to "A", "2021-10-26" to "A", "2021-10-27" to "A", "2021-10-28" to "A", "2021-10-29" to "A", "2021-10-30" to "A", "2021-10-31" to "A", "2021-11-01" to "A", "2021-11-02" to "B", "2021-11-03" to null, "2021-11-04" to "A", "2021-11-05" to "A", "2021-11-06" to null, "2021-11-07" to null, "2021-11-08" to "A", "2021-11-09" to "A", "2021-11-10" to "A", "2021-11-11" to "A", "2021-11-12" to "A", "2021-11-13" to "A", "2021-11-14" to null, "2021-11-15" to "A", "2021-11-16" to "A", "2021-11-17" to "A", "2021-11-18" to "A", "2021-11-19" to "A", "2021-11-20" to "A", "2021-11-21" to "A", "2021-11-22" to "A", "2021-11-23" to null, "2021-11-24" to "A", "2021-11-25" to "A", "2021-11-26" to "A", "2021-11-27" to "A", "2021-11-28" to "A", "2021-11-29" to "A", "2021-11-30" to "A", "2021-12-01" to "A", "2021-12-02" to "A", "2021-12-03" to "A", "2021-12-04" to null, "2021-12-05" to null, "2021-12-06" to "A", "2021-12-07" to "A", "2021-12-08" to "A", "2021-12-09" to "A", "2021-12-10" to "A", "2021-12-11" to null, "2021-12-12" to null, "2021-12-13" to "A", "2021-12-14" to "A", "2021-12-15" to "A", "2021-12-16" to "A", "2021-12-17" to "A", "2021-12-18" to "A", "2021-12-19" to null, "2021-12-20" to "A", "2021-12-21" to "A", "2021-12-22" to "A", "2021-12-23" to "A", "2021-12-24" to "A", "2021-12-25" to null, "2021-12-26" to null, "2021-12-27" to null, "2021-12-28" to null, "2021-12-29" to null, "2021-12-30" to null, "2021-12-31" to null, "2022-01-01" to null, "2022-01-02" to null, "2022-01-03" to null, "2022-01-04" to null, "2022-01-05" to null, "2022-01-06" to null, "2022-01-07" to "C", "2022-01-08" to null, "2022-01-09" to null, "2022-01-10" to null, "2022-01-11" to "A", "2022-01-12" to "A", "2022-01-13" to "A", "2022-01-14" to "A", "2022-01-15" to "A", "2022-01-16" to "A", "2022-01-17" to "A", "2022-01-18" to "A", "2022-01-19" to "A", "2022-01-20" to "A", "2022-01-21" to "A", "2022-01-22" to null, "2022-01-23" to null, "2022-01-24" to "A", "2022-01-25" to "A", "2022-01-26" to "C", "2022-01-27" to "A", "2022-01-28" to "A", "2022-01-29" to "A", "2022-01-30" to null, "2022-01-31" to "A", "2022-02-01" to "A", "2022-02-02" to "A", "2022-02-03" to "A", "2022-02-04" to "A", "2022-02-05" to null, "2022-02-06" to null, "2022-02-07" to "A", "2022-02-08" to "A", "2022-02-09" to "A", "2022-02-10" to "A", "2022-02-11" to "A", "2022-02-12" to null, "2022-02-13" to null, "2022-02-14" to "C", "2022-02-15" to "C", "2022-02-16" to "A", "2022-02-17" to "A", "2022-02-18" to "A", "2022-02-19" to null, "2022-02-20" to null, "2022-02-21" to "C", "2022-02-22" to "C", "2022-02-23" to null, "2022-02-24" to "C", "2022-02-25" to "C", "2022-02-26" to null, "2022-02-27" to null, "2022-02-28" to "C", "2022-03-01" to "A", "2022-03-02" to "A", "2022-03-03" to "A", "2022-03-04" to "A", "2022-03-05" to null, "2022-03-06" to null, "2022-03-07" to "C", "2022-03-08" to "C", "2022-03-09" to "C", "2022-03-10" to "C", "2022-03-11" to "C", "2022-03-12" to null, "2022-03-13" to null, "2022-03-14" to "C", "2022-03-15" to "C", "2022-03-16" to "C", "2022-03-17" to "C", "2022-03-18" to "C", "2022-03-19" to null, "2022-03-20" to null, "2022-03-21" to null, "2022-03-22" to "C", "2022-03-23" to "A", "2022-03-24" to "C", "2022-03-25" to "C", "2022-03-26" to null, "2022-03-27" to null, "2022-03-28" to "C", "2022-03-29" to "C", "2022-03-30" to "C", "2022-03-31" to "C")

    // 今日のダイヤを取り出す
    val daiya: String? = daiyaMap[today]
    // console.log(daiya);
    if (daiya != null) {
        Log.v("今日のダイヤ", daiya)
    }

//    // 今日のダイヤを基に運行時刻を選択
//    var toDaigaku:           // その日の全ての大学行きの出発時間(分)を入れる変数
//    var toYakusa =           // その日の全ての八草行きの出発時間(分)を入れる変数
//    var toDaigakuFirst =     // 最初の大学行きの出発時間(分)を入れる変数
//    var toYakusaFirst =      // 最初の八草行きの出発時間(分)を入れる変数

    when (daiya) {
        "A" -> return daiyaData(daiya, A1Map, A2Map, 0, 20)

        "B" -> return daiyaData(daiya, B1Map, B2Map, 0, 50)

        "C" -> return daiyaData(daiya, C1Map, C2Map, 10, 50)
    }

    // ABCのどのダイヤでもない時!!!
    return daiyaData(null, mapOf(0 to null), mapOf(0 to null), 0, 0)
}


fun getNextMin(timeTable: Map<Int, List<Int>?>, hour: Int): NextInfo {
    /*
    次の出発時間を調べる関数
    :param timeTable:   # array:時刻表(分)の配列データ
    :param hour:        # int：現在時間(時)
    :return:            # array：result  大学行きの今日の時刻表データ,
                        # int：hour      現在時間(時) or 現在時間+1(時)
                        # bool：flag     現在時間に+1されたかのフラグ
    */

    var result: Int = getNearVal(timeTable[hour], mode = 0)

    return if (result == -1) {
        // Triple(getNearVal(timeTable[hour + 1], mode = 1), hour+1, true)
        NextInfo(getNearVal(timeTable[hour + 1], mode = 1), hour+1, true)
    }else {
        // Triple(result, hour, false)
        NextInfo(result, hour, false)
    }
}


fun getAfterNextMin(flag: Boolean, timeTable: Map<Int, List<Int>?>, hour: Int, hour2: Int): AfterNextInfo {
    /*
    次の次の出発時間を調べる関数
    :param flag:        # 現在時間に+1されたかのフラグ
    :param timeTable:   # array:時刻表(分)の配列データ
    :param hour:        # int：現在時間(時)
    :param hour2:       # int：現在時間(時) or 現在時間+1(時)
    :return:            # int：result  次の次の出発時間(分),
                        # int：hour2   現在時間(時) or 現在時間+1(時) or 現在時間+2(時)
    */

    var result: Int
    var retHour: Int = hour2

    if (!flag) {
        result = getNearVal(timeTable[hour], mode = 3)
        if (result == -1) {
            result = getNearVal(timeTable[hour + 1], mode = 1)
            retHour += 1
        }
    } else {
        result = getNearVal(timeTable[hour + 1], mode = 3, reset = true);
        retHour += 1
        if (result == -1) {
            result = getNearVal(timeTable[hour + 2], mode = 1)
            retHour += 1
        }
    }
    return AfterNextInfo(result, retHour)
}


fun getAllData(): ReturnData {
    val todayInfo = getTodayTimetable()     // 今日の運行時刻表を取得(バスが無い日はnull, null, 0, 0が返ってくる)
    val hour: Int = LocalTime.now().hour  // 現在の時間(hour)を取得
    Log.v("hour", "hour=「" + hour + "」");

    //　大学行きの次のバスの出発時間を調べる(-1が帰ってきた場合はその時間のバスはもう無い)
    val toDaigakuInfo = getNextMin(todayInfo.toDaigaku, hour)
    var resultHour1_1 = hour

    //　八草行きの次のバスの出発時間を調べる(-1が帰ってきた場合はその時間のバスはもう無い)
    val toYakusaInfo = getNextMin(todayInfo.toYakusa, hour)
    var resultHour2_1 = hour

    // 次の次のバスの出発時刻を調べる(大学行き)
    val toDaigakuAfterNext = getAfterNextMin(toDaigakuInfo.flag, todayInfo.toDaigaku, hour, resultHour1_1)

    // 次の次のバスの出発時刻を調べる(八草行き)
    val toYakusaAfterNext = getAfterNextMin(toYakusaInfo.flag, todayInfo.toYakusa, hour, resultHour2_1)

    return ReturnData(todayInfo, toDaigakuInfo, toYakusaInfo, toDaigakuAfterNext, toYakusaAfterNext)
}