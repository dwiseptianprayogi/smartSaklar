package com.example.smartsaklar

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.allViews
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.example.smartsaklar.schedule.MainAdapterSchedule
import com.example.smartsaklar.schedule.ModelBarangSchedule
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import java.util.*

class MainAdapter(private val context: Context,
                  private val daftarBarang: ArrayList<ModelBarang?>?

) : RecyclerView.Adapter<MainViewHolder>() {


    private val listener: FirebaseDataListener
//    private val listener2: FirebaseDataListener2
    private lateinit var mListener: onItemClickListener

    private var mRecyclerViewSchedule: RecyclerView? = null
    private var mAdapterSchedule: MainAdapterSchedule? = null
    private var daftarSchedule: ArrayList<ModelBarangSchedule?>? = null
    private var mDatabaseReferenceSchedule: DatabaseReference? = null
    private var mFirebaseInstanceSchedule: FirebaseDatabase? = null

    private var mDatabaseReference: DatabaseReference? = null
    private var mFirebaseInstance: FirebaseDatabase? = null

    private var barang: ModelBarang? = null

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener:onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val generator = ColorGenerator.MATERIAL //custom color
        val color = generator.randomColor
//        holder.view.setCardBackgroundColor(color)
        holder.namaBarang.text = (daftarBarang?.get(position)?.nama)
        holder.btn1.text = (daftarBarang?.get(position)?.lampu1)
        holder.btn2.text = (daftarBarang?.get(position)?.lampu2)
//        holder.txtOption.text = "kk"
//        holder.btn.text = (daftarBarang?.get(position)?.lampu)

        val lampu1 = (daftarBarang?.get(position)?.lampu1)
        val lampu2 = (daftarBarang?.get(position)?.lampu2)

//        holder.btn.text = lampu1

        if (lampu1 == "off" && lampu2 == "off"){
            holder.btn.text = "off"
//            holder.btn.setBackgroundColor(Color.GRAY)
//            holder.btn.setBackgroundColor(R.drawable.rounded_btn)
            holder.btn.background = ContextCompat.getDrawable(context, R.drawable.rounded_btn)
        }
        if (lampu1 == "on" || lampu2 == "on"){
            holder.btn.text = "on"
//            holder.btn.setBackgroundColor(Color.GREEN)
            holder.btn.background = ContextCompat.getDrawable(context, R.drawable.rounded_btn2)
            holder.btn.setTextColor(Color.WHITE)
        }
        if (lampu1 == "on"){
            holder.btn1.background = ContextCompat.getDrawable(context, R.drawable.rounded_btn2)
            holder.btn1.setTextColor(Color.WHITE)
        }
        if (lampu2 == "on"){
            holder.btn2.background = ContextCompat.getDrawable(context, R.drawable.rounded_btn2)
            holder.btn2.setTextColor(Color.WHITE)
        }
        if (lampu1 == "off"){
            holder.btn1.background = ContextCompat.getDrawable(context, R.drawable.rounded_btn)
        }
        if (lampu2 == "off"){
            holder.btn2.background = ContextCompat.getDrawable(context, R.drawable.rounded_btn)
        }

//        holder.view.setOnClickListener { listener.onDataClick(daftarBarang?.get(position), position) }
        holder.btn.setOnClickListener { listener.onItemClick(daftarBarang?.get(position), position) }
        holder.btn1.setOnClickListener { listener.onItemClick2(daftarBarang?.get(position), position) }
        holder.btn2.setOnClickListener { listener.onItemClick3(daftarBarang?.get(position), position) }
        holder.addSchedule.setOnClickListener { listener.onItemClick6(daftarBarang?.get(position), position) }
        holder.showSchedule.setOnClickListener { listener.onItemClick7(daftarBarang?.get(position), position) }
        holder.txtOption.setOnClickListener {
            val popupMenu = PopupMenu(context, holder.txtOption)
            popupMenu.inflate(R.menu.option_menu)
            popupMenu.setOnMenuItemClickListener { item: MenuItem ->
                when (item.itemId) {
                    R.id.menu_edit -> {
                        listener.onItemClick4(daftarBarang?.get(position), position)
//                        dialogUpdateBarang(barang)
                    }
                    R.id.menu_remove -> {
                        listener.onItemClick5(daftarBarang?.get(position), position)
//                        hapusDataBarang(barang!!)
                    }
                }
                false
            }
            popupMenu.show()

        }

        val txt = holder.linear
        txt.visibility = View.GONE

        var hideShow = 0
        holder.showHide.setOnClickListener {
//            Toast.makeText(context, "show :"+txt2,Toast.LENGTH_SHORT).show()

//            val args = Bundle()
//            val jadwal = holder.schedule.toString()
//            args.putString("key", jadwal)
//
//            holder.showHide.text = "Hide More Option ⬆"
//            holder.schedule.setOnClickListener { listener.onItemClick6(daftarBarang?.get(position), position)}
//            mRecyclerViewSchedule = holder.schedule
//            mRecyclerViewSchedule!!.setHasFixedSize(true)
//            mRecyclerViewSchedule!!.setLayoutManager(LinearLayoutManager(context))
//            FirebaseApp.initializeApp(context)
//
//            mFirebaseInstance = FirebaseDatabase.getInstance()
//            mDatabaseReference = mFirebaseInstance!!.getReference("smartButton")
//            mDatabaseReference!!.child("data_ruangan")
//                .addValueEventListener(object : ValueEventListener {
//                    override fun onDataChange(dataSnapshot: DataSnapshot) {
//                        for (mDataSnapshot in dataSnapshot.children) {
//                            val barang1 = mDataSnapshot.getValue(ModelBarang::class.java)!!
//                            barang1.key = mDataSnapshot.key
////                            mDatabaseReferenceSchedule = mFirebaseInstanceSchedule!!.getReference("smartButton")
//                            mDatabaseReference!!.child("data_ruangan").child(barang1.key!!).child("schedule")
//                                .addValueEventListener(object : ValueEventListener {
//                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
//                                        daftarSchedule = ArrayList()
//                                        for (mDataSnapshot in dataSnapshot.children) {
//                                            val barang = mDataSnapshot.getValue(ModelBarangSchedule::class.java)!!
//                                            barang.key = mDataSnapshot.key
//                                            daftarSchedule!!.add(barang)
//                                        }
//                                        mAdapterSchedule = MainAdapterSchedule(context, daftarSchedule)
//                                        mRecyclerViewSchedule!!.setAdapter(mAdapterSchedule)
//                                    }
//                                    override fun onCancelled(databaseError: DatabaseError) {
//                                        Toast.makeText(
//                                            context,
//                                            databaseError.details + " " + databaseError.message, Toast.LENGTH_LONG
//                                        ).show()
//                                    }
//                                })
////                            daftarBarang!!.add(barang)
//                        }
////                        mAdapter = MainAdapter(this@MainActivity, daftarBarang)
////                        mRecyclerView!!.setAdapter(mAdapter)
//                    }
//
//                    override fun onCancelled(databaseError: DatabaseError) {
//                        Toast.makeText(
//                            context,
//                            databaseError.details + " " + databaseError.message, Toast.LENGTH_LONG
//                        ).show()
//                    }
//                })

//            mFirebaseInstanceSchedule = FirebaseDatabase.getInstance()
//            mDatabaseReferenceSchedule = mFirebaseInstanceSchedule!!.getReference("smartButton")
//            mDatabaseReferenceSchedule!!.child("data_ruangan").child(barang!!.key!!).child("schedule")
//                .addValueEventListener(object : ValueEventListener {
//                    override fun onDataChange(dataSnapshot: DataSnapshot) {
//                        daftarSchedule = ArrayList()
//                        for (mDataSnapshot in dataSnapshot.children) {
//                            val barang = mDataSnapshot.getValue(ModelBarangSchedule::class.java)!!
//                            barang.key = mDataSnapshot.key
//                            daftarSchedule!!.add(barang)
//                        }
//                        mAdapterSchedule = MainAdapterSchedule(context, daftarSchedule)
//                        mRecyclerViewSchedule!!.setAdapter(mAdapterSchedule)
//                    }
//                    override fun onCancelled(databaseError: DatabaseError) {
//                        Toast.makeText(
//                            context,
//                            databaseError.details + " " + databaseError.message, Toast.LENGTH_LONG
//                        ).show()
//                    }
//                })

            hideShow++
            if(hideShow == 1){
                txt.visibility = View.VISIBLE
                holder.showHide.text = "Hide More Option ⬆"
//                listener.onItemClick7(daftarBarang?.get(position), position)
//                listener.onItemClick7(daftarBarang?.get(position), position)
            }
            if(hideShow == 2){
                txt.visibility = View.GONE
                holder.showHide.text = "Show More Option ⬇"
                hideShow = 0
            }
        }

//        holder.titleSchedule?.text = "Schedule for "+ (daftarBarang?.get(position)?.nama)
//        holder.schedule.setOnCreateContextMenuListener {
//                contextMenu, view, contextMenuInfo ->

//        }


    }


    override fun getItemCount(): Int {

        return daftarBarang?.size!!
    }

    //interface data listener
    interface FirebaseDataListener {
//        fun onDataClick(barang: ModelBarang?, position: Int)
        fun onItemClick(barang:ModelBarang?, position: Int)
        fun onItemClick2(barang:ModelBarang?, position: Int)
        fun onItemClick3(barang:ModelBarang?, position: Int)
        fun onItemClick4(barang:ModelBarang?, position: Int)
        fun onItemClick5(barang:ModelBarang?, position: Int)
        fun onItemClick6(barang:ModelBarang?, position: Int)
        fun onItemClick7(barang:ModelBarang?, position: Int)
    }

    init {
        listener = context as FirebaseDataListener
    }

//    interface FirebaseDataListener2 {
////        fun onDataClick(barang: ModelBarang?, position: Int)
//        fun onItemClick(barang:ModelBarang?, position: Int)
//    }
//
//    init {
//        listener2 = context as FirebaseDataListener2
//    }

}