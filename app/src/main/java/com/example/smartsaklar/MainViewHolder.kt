package com.example.smartsaklar

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    @JvmField
    var namaBarang: TextView = itemView.findViewById(R.id.mTitle)

    @JvmField
    var showHide: TextView = itemView.findViewById(R.id.showOrHideBtn)

    @JvmField
    var linear: LinearLayout = itemView.findViewById(R.id.LinearLayoutCv)

    @JvmField
    var btn: Button = itemView.findViewById(R.id.btnLampu)

    @JvmField
    var btn1: Button = itemView.findViewById(R.id.btnLampu1)

    @JvmField
    var btn2: Button = itemView.findViewById(R.id.btnLampu2)

    @JvmField
    var txtOption: TextView = itemView.findViewById(R.id.txt_option)

    @JvmField
    var view: CardView = itemView.findViewById(R.id.cv)

//    @JvmField
//    var schedule: RecyclerView = itemView.findViewById(R.id.rvSchedule)

    @JvmField
    var addSchedule: Button = itemView.findViewById(R.id.btnSchedule)

    @JvmField
    var showSchedule: Button = itemView.findViewById(R.id.btnScheduleShow)

    @JvmField
    var idStat: TextView = itemView.findViewById(R.id.idStat)

//    @JvmField
//    var deleteSchedule: ImageView = itemView.findViewById(R.id.imgShceduleDelete)

//    @JvmField
//    var addSchedule: Button = itemView.findViewById(R.id.rvSchedule)

//    @JvmField
//    var titleSchedule: TextView = itemView.findViewById(R.id.titleSchedule)

}