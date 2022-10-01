package com.example.smartsaklar.schedule

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.util.*

class MainAdapterSchedule(private val context: Context,
                          private val daftarSchedule: ArrayList<ModelBarangSchedule?>?
) : RecyclerView.Adapter<MainViewHolderSchedule>() {


    private val listener: FirebaseDataListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolderSchedule {

        val view = LayoutInflater.from(parent.context).inflate(com.example.smartsaklar.R.layout.item_schedule, parent, false)
        return MainViewHolderSchedule(view)
    }

    override fun onBindViewHolder(holder: MainViewHolderSchedule, position: Int) {
        holder.timeSchedule.text = (daftarSchedule?.get(position)?.Time)
        holder.stateSchedule.text = (daftarSchedule?.get(position)?.State)

        holder.deleteSchedule.setOnClickListener { listener.onItemClickSceduleDelete(daftarSchedule?.get(position), position) }
        holder.editSchedule.setOnClickListener { listener.onItemClickSceduleEdit(daftarSchedule?.get(position), position) }
//        holder.addSchedule.setOnClickListener { listener.onItemClickSceduleAdd(daftarSchedule?.get(position), position) }
    }

    override fun getItemCount(): Int {
        return daftarSchedule?.size!!
    }

    //interface data listener
    interface FirebaseDataListener {
//        fun onItemClickSceduleAdd(barangSchedule:ModelBarangSchedule?, position: Int)
        fun onItemClickSceduleEdit(barangSchedule:ModelBarangSchedule?, position: Int)
        fun onItemClickSceduleDelete(barangSchedule:ModelBarangSchedule?, position: Int)
    }

    init {
        listener = context as FirebaseDataListener
    }


}