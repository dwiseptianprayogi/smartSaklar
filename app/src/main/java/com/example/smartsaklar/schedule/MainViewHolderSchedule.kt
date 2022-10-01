package com.example.smartsaklar.schedule

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartsaklar.R

class MainViewHolderSchedule(itemView: View) : RecyclerView.ViewHolder(itemView) {

    @JvmField
    var timeSchedule: TextView = itemView.findViewById(R.id.tvSchedulJam)

    @JvmField
    var stateSchedule: TextView = itemView.findViewById(R.id.tvSchedulState)

    @JvmField
    var editSchedule: ImageView = itemView.findViewById(R.id.imgShceduleEdit)

    @JvmField
    var deleteSchedule: ImageView = itemView.findViewById(R.id.imgShceduleDelete)

//    @JvmField
//    var addSchedule: Button = itemView.findViewById(R.id.btnSchedule)

}