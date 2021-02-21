package com.example.aitbusinfo

import android.util.Log
import android.widget.TextView
import com.google.android.material.tabs.TabLayout
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
    //val today = "2020-6-18"
    val today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    // 時刻表データを定義(JSON形式)
    val A1Map = mapOf(0 to null, 1 to null, 2 to null, 3 to null, 4 to null, 5 to null, 6 to null, 7 to null, 8 to listOf(0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55), 9 to listOf(0, 5, 10, 15, 20, 25, 30, 35, 40, 50, 55), 10 to listOf(0, 5, 10, 15, 20, 25, 30, 35, 45, 55), 11 to listOf(5, 15, 25, 35, 45, 55), 12 to listOf(5, 15, 25, 35, 45, 55), 13 to listOf(5, 20, 35, 50), 14 to listOf(5, 15, 25, 35, 45, 55), 15 to listOf(5, 15, 30, 45), 16 to listOf(0, 15, 30, 45), 17 to listOf(0, 15, 30, 45), 18 to listOf(0, 15, 45), 19 to listOf(0, 15, 30, 45), 20 to listOf(0, 30), 21 to listOf(0), 22 to null, 23 to null)
    val A2Map = mapOf(0 to null,1 to null,2 to null,3 to null,4 to null,5 to null,6 to null,7 to null,8 to listOf(20,50),9 to listOf(20,50),10 to listOf(20,50),11 to listOf(0,10,20,30,40,50),12 to listOf(0,10,20,30,40,50),13 to listOf(0,15,30,45),14 to listOf(0,10,20,30,40,45,50,55),15 to listOf(0,10,20,30,40,50),16 to listOf(0,5,10,15,25,30,35,40,45,50,55),17 to listOf(0,5,10,15,30,35,40,45,55),18 to listOf(0,10,20,30,40,50),19 to listOf(0,15,30,45),20 to listOf(0,15,30,45),21 to listOf(0,15,30,45),22 to null,23 to null)
    val B1Map = mapOf(0 to null,1 to null,2 to null,3 to null,4 to null,5 to null,6 to null,7 to null,8 to listOf(0,10,20,35,45),9 to listOf(0,5,25,35,50),10 to listOf(0,10,30,55),11 to listOf(0,25,50),12 to listOf(25),13 to listOf(35),14 to listOf(5,35),15 to listOf(5,35),16 to listOf(5,45),17 to listOf(10,45),18 to listOf(5,35),19 to listOf(35),20 to listOf(25),21 to listOf(5),22 to null,23 to null)
    val B2Map = mapOf(0 to null,1 to null,2 to null,3 to null,4 to null,5 to null,6 to null,7 to null,8 to listOf(50),9 to listOf(40),10 to listOf(5,50),11 to listOf(15,40),12 to listOf(10),13 to listOf(20,50),14 to listOf(20,50),15 to listOf(20,50),16 to listOf(20),17 to listOf(0,30,55),18 to listOf(20,50),19 to listOf(20,50),20 to listOf(40),21 to listOf(30),22 to null,23 to null)
    val C1Map = mapOf(0 to null,1 to null,2 to null,3 to null,4 to null,5 to null,6 to null,7 to null,8 to listOf(10,35),9 to listOf(0,25,50),10 to listOf(10,55),11 to listOf(25,50),12 to listOf(25),13 to listOf(35),14 to listOf(5,35),15 to listOf(5,35),16 to listOf(5,45),17 to listOf(10,45),18 to listOf(5,35),19 to listOf(35),20 to listOf(25),21 to listOf(5),22 to null,23 to null)
    val C2Map = mapOf(0 to null,1 to null,2 to null,3 to null,4 to null,5 to null,6 to null,7 to null,8 to listOf(50),9 to listOf(40),10 to listOf(5,50),11 to listOf(15,40),12 to listOf(10),13 to listOf(20,50),14 to listOf(20,50),15 to listOf(20,50),16 to listOf(20),17 to listOf(0,30,55),18 to listOf(20,50),19 to listOf(20,50),20 to listOf(40),21 to listOf(30),22 to null,23 to null)
    val daiyaMap = mapOf("2020-5-1" to "C", "2020-5-2" to "C", "2020-5-3" to null, "2020-5-4" to "C", "2020-5-5" to "C", "2020-5-6" to "C", "2020-5-7" to "C", "2020-5-8" to "C", "2020-5-9" to "C", "2020-5-10" to null, "2020-5-11" to "C", "2020-5-12" to "C", "2020-5-13" to "C", "2020-5-14" to "C", "2020-5-15" to "C", "2020-5-16" to "C", "2020-5-17" to null, "2020-5-18" to "C", "2020-5-19" to "C", "2020-5-20" to "C", "2020-5-21" to "C", "2020-5-22" to "C", "2020-5-23" to "C", "2020-5-24" to null, "2020-5-25" to "C", "2020-5-26" to "C", "2020-5-27" to "C", "2020-5-28" to "C", "2020-5-29" to "C", "2020-5-30" to "C", "2020-5-31" to null, "2020-6-1" to "A", "2020-6-2" to "A", "2020-6-3" to "A", "2020-6-4" to "A", "2020-6-5" to "A", "2020-6-6" to "C", "2020-6-7" to null, "2020-6-8" to "A", "2020-6-9" to "A", "2020-6-10" to "A", "2020-6-11" to "A", "2020-6-12" to "A", "2020-6-13" to "C", "2020-6-14" to null, "2020-6-15" to "A", "2020-6-16" to "A", "2020-6-17" to "A", "2020-6-18" to "A", "2020-6-19" to "A", "2020-6-20" to "C", "2020-6-21" to null, "2020-6-22" to "A", "2020-6-23" to "A", "2020-6-24" to "A", "2020-6-25" to "A", "2020-6-26" to "A", "2020-6-27" to "C", "2020-6-28" to null, "2020-6-29" to "A", "2020-6-30" to "A", "2020-7-1" to "A", "2020-7-2" to "A", "2020-7-3" to "A", "2020-7-4" to "C", "2020-7-5" to null, "2020-7-6" to "A", "2020-7-7" to "A", "2020-7-8" to "A", "2020-7-9" to "A", "2020-7-10" to "A", "2020-7-11" to "C", "2020-7-12" to null, "2020-7-13" to "A", "2020-7-14" to "A", "2020-7-15" to "A", "2020-7-16" to "A", "2020-7-17" to "A", "2020-7-18" to "A", "2020-7-19" to "A", "2020-7-20" to "A", "2020-7-21" to "A", "2020-7-22" to "A", "2020-7-23" to "A", "2020-7-24" to "A", "2020-7-25" to "A", "2020-7-26" to "A", "2020-7-27" to "A", "2020-7-28" to "A", "2020-7-29" to "A", "2020-7-30" to "A", "2020-7-31" to "A", "2020-8-1" to "A", "2020-8-2" to null, "2020-8-3" to "A", "2020-8-4" to "A", "2020-8-5" to "A", "2020-8-6" to "A", "2020-8-7" to "A", "2020-8-8" to "C", "2020-8-9" to null, "2020-8-10" to "C", "2020-8-11" to "C", "2020-8-12" to "C", "2020-8-13" to "C", "2020-8-14" to null, "2020-8-15" to null, "2020-8-16" to null, "2020-8-17" to "C", "2020-8-18" to "B", "2020-8-19" to "B", "2020-8-20" to "B", "2020-8-21" to "C", "2020-8-22" to "C", "2020-8-23" to null, "2020-8-24" to "C", "2020-8-25" to "C", "2020-8-26" to "C", "2020-8-27" to "C", "2020-8-28" to "C", "2020-8-29" to "C", "2020-8-30" to null, "2020-8-31" to "C", "2020-9-1" to "C", "2020-9-2" to "C", "2020-9-3" to "C", "2020-9-4" to "C", "2020-9-5" to "C", "2020-9-6" to null, "2020-9-7" to "C", "2020-9-8" to "C", "2020-9-9" to "C", "2020-9-10" to "C", "2020-9-11" to "C", "2020-9-12" to "C", "2020-9-13" to null, "2020-9-14" to "C", "2020-9-15" to "C", "2020-9-16" to "A", "2020-9-17" to "B", "2020-9-18" to "A", "2020-9-19" to "C", "2020-9-20" to null, "2020-9-21" to "A", "2020-9-22" to null, "2020-9-23" to "A", "2020-9-24" to "A", "2020-9-25" to "A", "2020-9-26" to "C", "2020-9-27" to null, "2020-9-28" to "A", "2020-9-29" to "A", "2020-9-30" to "A", "2020-10-1" to "A", "2020-10-2" to "A", "2020-10-3" to "C", "2020-10-4" to null, "2020-10-5" to "A", "2020-10-6" to "A", "2020-10-7" to "A", "2020-10-8" to "B", "2020-10-9" to "B", "2020-10-10" to "A", "2020-10-11" to "A", "2020-10-12" to "C", "2020-10-13" to "A", "2020-10-14" to "A", "2020-10-15" to "A", "2020-10-16" to "A", "2020-10-17" to "C", "2020-10-18" to null, "2020-10-19" to "A", "2020-10-20" to "A", "2020-10-21" to "A", "2020-10-22" to "A", "2020-10-23" to "A", "2020-10-24" to "A", "2020-10-25" to null, "2020-10-26" to "A", "2020-10-27" to "A", "2020-10-28" to "A", "2020-10-29" to "A", "2020-10-30" to "A", "2020-10-31" to "C", "2020-11-1" to null, "2020-11-2" to "A", "2020-11-3" to "A", "2020-11-4" to "A", "2020-11-5" to "A", "2020-11-6" to "A", "2020-11-7" to "C", "2020-11-8" to null, "2020-11-9" to "A", "2020-11-10" to "A", "2020-11-11" to "B", "2020-11-12" to "A", "2020-11-13" to null, "2020-11-14" to "A", "2020-11-15" to null, "2020-11-16" to "A", "2020-11-17" to "A", "2020-11-18" to "A", "2020-11-19" to "A", "2020-11-20" to "A", "2020-11-21" to "A", "2020-11-22" to "A", "2020-11-23" to "A", "2020-11-24" to "A", "2020-11-25" to "A", "2020-11-26" to "A", "2020-11-27" to "A", "2020-11-28" to "A", "2020-11-29" to null, "2020-11-30" to "A", "2020-12-1" to "A", "2020-12-2" to "A", "2020-12-3" to "A", "2020-12-4" to "A", "2020-12-5" to "C", "2020-12-6" to null, "2020-12-7" to "A", "2020-12-8" to "A", "2020-12-9" to "A", "2020-12-10" to "A", "2020-12-11" to "A", "2020-12-12" to "C", "2020-12-13" to null, "2020-12-14" to "A", "2020-12-15" to "A", "2020-12-16" to "A", "2020-12-17" to "A", "2020-12-18" to "A", "2020-12-19" to "A", "2020-12-20" to null, "2020-12-21" to "A", "2020-12-22" to "A", "2020-12-23" to "A", "2020-12-24" to "A", "2020-12-25" to "A", "2020-12-26" to null, "2020-12-27" to null, "2020-12-28" to null, "2020-12-29" to null, "2020-12-30" to null, "2020-12-31" to null, "2021-1-1" to null, "2021-1-2" to null, "2021-1-3" to null, "2021-1-4" to null, "2021-1-5" to null, "2021-1-6" to null, "2021-1-7" to "C", "2021-1-8" to "C", "2021-1-9" to "C", "2021-1-10" to null, "2021-1-11" to "A", "2021-1-12" to "A", "2021-1-13" to "A", "2021-1-14" to "A", "2021-1-15" to "A", "2021-1-16" to "A", "2021-1-17" to "A", "2021-1-18" to "A", "2021-1-19" to "A", "2021-1-20" to "A", "2021-1-21" to "A", "2021-1-22" to "A", "2021-1-23" to "C", "2021-1-24" to null, "2021-1-25" to "A", "2021-1-26" to null, "2021-1-27" to "A", "2021-1-28" to "A", "2021-1-29" to "A", "2021-1-30" to "C", "2021-1-31" to null, "2021-2-1" to "A", "2021-2-2" to "A", "2021-2-3" to "A", "2021-2-4" to "A", "2021-2-5" to "A", "2021-2-6" to "C", "2021-2-7" to null, "2021-2-8" to "A", "2021-2-9" to "A", "2021-2-10" to "A", "2021-2-11" to null, "2021-2-12" to "C", "2021-2-13" to "C", "2021-2-14" to null, "2021-2-15" to "A", "2021-2-16" to "A", "2021-2-17" to "A", "2021-2-18" to "A", "2021-2-19" to "A", "2021-2-20" to "C", "2021-2-21" to null, "2021-2-22" to "C", "2021-2-23" to null, "2021-2-24" to "C", "2021-2-25" to "C", "2021-2-26" to "C", "2021-2-27" to "C", "2021-2-28" to null, "2021-3-1" to "A", "2021-3-2" to "A", "2021-3-3" to "A", "2021-3-4" to "A", "2021-3-5" to "A", "2021-3-6" to "C", "2021-3-7" to null, "2021-3-8" to "C", "2021-3-9" to "C", "2021-3-10" to "C", "2021-3-11" to "C", "2021-3-12" to "C", "2021-3-13" to "C", "2021-3-14" to null, "2021-3-15" to "C", "2021-3-16" to "C", "2021-3-17" to "C", "2021-3-18" to "C", "2021-3-19" to "C", "2021-3-20" to null, "2021-3-21" to null, "2021-3-22" to "C", "2021-3-23" to "A", "2021-3-24" to "C", "2021-3-25" to "C", "2021-3-26" to "C", "2021-3-27" to "C", "2021-3-28" to null, "2021-3-29" to "C", "2021-3-30" to "C", "2021-3-31" to "C")

    // 今日のダイヤを取り出す
    val daiya: String? = daiyaMap[today]
    // console.log(daiya);

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
    Log.v("テスト", "これはメッセージです「" + hour + "」終わり");

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