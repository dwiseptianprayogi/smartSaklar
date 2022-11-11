package com.example.smartsaklar

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import android.widget.TimePicker.OnTimeChangedListener
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartsaklar.schedule.MainAdapterSchedule
import com.example.smartsaklar.schedule.ModelBarangSchedule
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import org.json.JSONArray
import org.json.JSONTokener
import java.time.Month
import java.time.Year
import java.util.*


class MainActivity : AppCompatActivity(), MainAdapter.FirebaseDataListener, MainAdapterSchedule.FirebaseDataListener {

//    private var mEditNama: EditText? = null
    private var idAlat: EditText? = null
    private var namaWifi:EditText? = null
    private var passWifi:EditText? = null

    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: MainAdapter? = null
    private var daftarBarang: ArrayList<ModelBarang?>? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabaseReference2: DatabaseReference? = null
    private var mFirebaseInstance: FirebaseDatabase? = null

    private var timeLamp : String? = null
    private var dateLamp : String? = null
    private var dateLampId : String? = null
    private var hour : String? = null
    private var minute : String? = null

    private var day : String? = null
    private var month : String? = null
    private var year : String? = null

    private var dayEdit:String? = null
    private var monthEdit:String? = null
    private var yearEdit:String? = null
    private var hourEdit:String? = null
    private var minuteEdit:String? = null

    private var stateLamp2:String? = null

    private var idRuanganSchedule : String? = null

    private var idRuangan : String? = null

    private var mRecyclerViewSchedule: RecyclerView? = null
    private var mAdapterSchedule: MainAdapterSchedule? = null
    private var daftarSchedule: ArrayList<ModelBarangSchedule?>? = null
    private var mDatabaseReferenceSchedule: DatabaseReference? = null
    private var mFirebaseInstanceSchedule: FirebaseDatabase? = null

    lateinit var addData: FloatingActionButton
    lateinit var addWifi: FloatingActionButton
    var fabVisible = false
    lateinit var mFloatingActionButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = Color.TRANSPARENT
        mRecyclerView = findViewById(R.id.recycler_view)
        mRecyclerView!!.setHasFixedSize(true)
        mRecyclerView!!.setLayoutManager(LinearLayoutManager(this))
        FirebaseApp.initializeApp(this)
        mFirebaseInstance = FirebaseDatabase.getInstance()
        mDatabaseReference = mFirebaseInstance!!.getReference("smartButton")
        mDatabaseReference!!.child("new").child("ruangan")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    daftarBarang = ArrayList()
                    for (mDataSnapshot in dataSnapshot.children) {
                        val barang = mDataSnapshot.getValue(ModelBarang::class.java)!!
                        barang.key = mDataSnapshot.key
                        daftarBarang!!.add(barang)
                    }
                    mAdapter = MainAdapter(this@MainActivity, daftarBarang)
                    mRecyclerView!!.setAdapter(mAdapter)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(
                        this@MainActivity,
                        databaseError.details + " " + databaseError.message, Toast.LENGTH_LONG
                    ).show()
                }
            })
        fabVisible = false
        addData = findViewById(R.id.idFABHome)
        addWifi = findViewById(R.id.idFABSettings)
        mFloatingActionButton = findViewById(R.id.tambah_barang)
        mFloatingActionButton.setImageDrawable(resources.getDrawable(R.drawable.ic_add))

        mFloatingActionButton!!.setOnClickListener(View.OnClickListener {
//            dialogTambahBarang()
            if (!fabVisible) {
                addData.show()
                addWifi.show()

                addWifi.visibility = View.VISIBLE
                addData.visibility = View.VISIBLE
                fabVisible = true

                mFloatingActionButton.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_cancel_24))
            } else {
                addData.hide()
                addWifi.hide()

                addWifi.visibility = View.GONE
                addData.visibility = View.GONE
                fabVisible = false

                mFloatingActionButton.setImageDrawable(resources.getDrawable(R.drawable.ic_add))
            }
        })

        addData.setOnClickListener{
            dialogTambahBarang()
        }
        addWifi.setOnClickListener{
            dialogTambahWifi()
        }

//        val bottomSheetFragment = BottomSheetFragment()
//        BottomSheetBehavior.from(findViewById(R.id.sheet)).apply {
//            peekHeight=700
//            this.state = BottomSheetBehavior.STATE_COLLAPSED
//        }

        val timePicker = findViewById<TimePicker>(R.id.timePicker)
        timePicker.setIs24HourView(true)

        val schedule = findViewById<FrameLayout>(R.id.sheet)
        schedule.visibility = View.GONE

        val schedule1 = findViewById<FrameLayout>(R.id.sheetSchedule)
        schedule1.visibility = View.GONE

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        toogle = ActionBarDrawerToggle(this, drawerLayout, com.google.firebase.database.R.string.open , com.google.firebase.database.R.string.close )
//        drawerLayout.addDrawerListener(toogle)
//        toogle.syncState()
    }

    override fun onItemClick(barang: ModelBarang?, position: Int) {
        Toast.makeText(this, "show : "+ position,Toast.LENGTH_SHORT).show()
        val btn1 = barang!!.lampu1
        val btn2 = barang.lampu2
        if (btn1=="on"||btn2=="on"){
            barang.lampu1 = "off"
            barang.lampu2 = "off"
            val nama = barang.nama

            mDatabaseReference!!.child("new/state").setValue("1")
            mDatabaseReference!!.child("new/ruangan").child(barang!!.key!!).child("lampu1")
                .setValue(barang.lampu1).addOnSuccessListener {
                    Toast.makeText(
                        this@MainActivity,
                        "Data berhasil di update !",
                        Toast.LENGTH_LONG
                    ).show()
                }
            mDatabaseReference!!.child("new/ruangan").child(barang!!.key!!).child("lampu2")
                .setValue(barang.lampu2).addOnSuccessListener {
                    Toast.makeText(
                        this@MainActivity,
                        "Data berhasil di update !",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
        if (btn1=="off"&& btn2=="off"){
            barang.lampu1 = "on"
            barang.lampu2 = "on"
//            updateDataBarang(barang)
            val nama = barang.nama
            mDatabaseReference!!.child("new/state").setValue("1")
            mDatabaseReference!!.child("new/ruangan").child(barang!!.key!!).child("lampu1")
                .setValue(barang.lampu1).addOnSuccessListener {
                    Toast.makeText(
                        this@MainActivity,
                        "Data berhasil di update !",
                        Toast.LENGTH_LONG
                    ).show()
                }
            mDatabaseReference!!.child("new/ruangan").child(barang!!.key!!).child("lampu2")
                .setValue(barang.lampu2).addOnSuccessListener {
                    Toast.makeText(
                        this@MainActivity,
                        "Data berhasil di update !",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }

    override fun onItemClick2(barang: ModelBarang?, position: Int) {
//        barang.nama = mEditNama!!.getText().toString()
        val btn1 = barang!!.lampu1
        if (btn1=="off"){
            barang.lampu1 = "on"
//            updateDataBarang(barang)
            mDatabaseReference!!.child("new/state").setValue("1")
            mDatabaseReference!!.child("new/ruangan").child(barang!!.key!!).child("lampu1")
                .setValue(barang.lampu1).addOnSuccessListener {
                    Toast.makeText(
                        this@MainActivity,
                        "Data berhasil di update !",
                        Toast.LENGTH_LONG
                    ).show()
                }
            val notificationHelper = NotificationHelper(this@MainActivity)
            notificationHelper.sendHighPriorityNotification("Lampu 1 Mati Mohon Ganti!",
                "", MainActivity::class.java)
        }
        if (btn1=="on"){
            barang.lampu1 = "off"
//            updateDataBarang(barang)
            mDatabaseReference!!.child("state").setValue("1")
            mDatabaseReference!!.child("new/ruangan").child(barang!!.key!!).child("lampu1")
                .setValue(barang.lampu1).addOnSuccessListener {
                    Toast.makeText(
                        this@MainActivity,
                        "Data berhasil di update !",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }

    override fun onItemClick3(barang: ModelBarang?, position: Int) {
        val btn1 = barang!!.lampu2
        if (btn1=="off"){
            barang.lampu2 = "on"
//            updateDataBarang(barang)
            mDatabaseReference!!.child("new/state").setValue("1")
            mDatabaseReference!!.child("new/ruangan").child(barang!!.key!!).child("lampu2")
                .setValue(barang.lampu2).addOnSuccessListener {
                    Toast.makeText(
                        this@MainActivity,
                        "Data berhasil di update !",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
        if (btn1=="on"){
            barang.lampu2 = "off"
//            updateDataBarang(barang)
            mDatabaseReference!!.child("new/state").setValue("1")
            mDatabaseReference!!.child("new/ruangan").child(barang!!.key!!).child("lampu2")
                .setValue(barang.lampu2).addOnSuccessListener {
                    Toast.makeText(
                        this@MainActivity,
                        "Data berhasil di update !",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }

    }
    override fun onItemClick4(barang: ModelBarang?, position: Int) {
        dialogUpdateBarang(barang)
    }
    override fun onItemClick5(barang: ModelBarang?, position: Int) {
        hapusDataBarang(barang!!)
    }

    override fun onItemClick6(barang: ModelBarang?, position: Int) {
        val schedule = findViewById<FrameLayout>(R.id.sheet)
        schedule.visibility = View.VISIBLE
        val imgCancelSchedule = findViewById<ImageView>(R.id.imgShceduleCancel)
        imgCancelSchedule.setOnClickListener {
            schedule.visibility = View.GONE
        }
        val tvSaveSchedule = findViewById<TextView>(R.id.tvSchedulSave)
        val datePicker = findViewById<DatePicker>(R.id.date_Picker)
        val today = Calendar.getInstance()
        year = today.get(Calendar.YEAR).toString()
        month = today.get(Calendar.MONTH).toString()
        day =  today.get(Calendar.DAY_OF_MONTH).toString()
        dateLamp = day+"/"+(month!!.toInt()+1)+"/"+year
        dateLampId = day+(month!!.toInt()+1)+year
        datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ) { view, year, month, day ->
            val month = month + 1
            val msg = "You Selected: $day/$month/$year"
            dateLamp = day.toString()+"/"+month+"/"+year
//            Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
        }
        val simpleTimePicker = findViewById<View>(R.id.timePicker) as TimePicker
        simpleTimePicker.setIs24HourView(true) // used to display AM/PM mode
        hour = simpleTimePicker.hour.toString()
        minute = simpleTimePicker.minute.toString()
        simpleTimePicker.setOnTimeChangedListener(OnTimeChangedListener { view, hourOfDay, minute ->
//            Toast.makeText(applicationContext, "$hourOfDay  $minute", Toast.LENGTH_SHORT).show()
            timeLamp = hourOfDay.toString()+":"+minute
        })
        timeLamp = hour +":"+ minute

        tvSaveSchedule.setOnClickListener {
            dialogSchedule(barang)
        }
    }

    override fun onItemClick7(barang: ModelBarang?, position: Int) {
        val schedule = findViewById<FrameLayout>(R.id.sheetSchedule)
        schedule.visibility = View.VISIBLE
        val imgCancelSchedule = findViewById<ImageView>(R.id.imgShceduleCancelShow)
        imgCancelSchedule.setOnClickListener {
            schedule.visibility = View.GONE
        }

        idRuanganSchedule = barang!!.key!!

        Toast.makeText(this, idRuanganSchedule, Toast.LENGTH_SHORT).show()

        mRecyclerViewSchedule = findViewById(R.id.rvSchedule)
        mRecyclerViewSchedule!!.setHasFixedSize(true)
        mRecyclerViewSchedule!!.setLayoutManager(LinearLayoutManager(this))
        FirebaseApp.initializeApp(this)
        mFirebaseInstanceSchedule = FirebaseDatabase.getInstance()
        mDatabaseReferenceSchedule = mFirebaseInstanceSchedule!!.getReference("smartButton")
        mDatabaseReferenceSchedule!!.child("new/ruangan").child(barang!!.key!!).child("schedule")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    daftarSchedule = ArrayList()
                    for (mDataSnapshot in dataSnapshot.children) {
                        val barang = mDataSnapshot.getValue(ModelBarangSchedule::class.java)!!
                        barang.key = mDataSnapshot.key
                        daftarSchedule!!.add(barang)
                    }
                    mAdapterSchedule = MainAdapterSchedule(this@MainActivity, daftarSchedule)
                    mRecyclerViewSchedule!!.setAdapter(mAdapterSchedule)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(
                        this@MainActivity,
                        databaseError.details + " " + databaseError.message, Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
//    override fun onItemClick8(barang: ModelBarang?, position: Int) {
////        mDatabaseReference!!.child("new/state").setValue("1")
//        mDatabaseReference!!.child("new/update/id").setValue(barang!!.key!!)
//            .addOnSuccessListener {
//                Toast.makeText(
//                    this@MainActivity,
//                    "Please Turn On The Smart Switch!",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//        mDatabaseReference!!.child("new/update/state").setValue("1")
//    }

    override fun onItemClickSceduleEdit(barang: ModelBarangSchedule?, position: Int) {
        Toast.makeText(this, idRuanganSchedule, Toast.LENGTH_SHORT).show()
//        val btnState = findViewById<ToggleButton>(R.id.btnScheduleState)
//
//        mDatabaseReference!!.child("new/ruangan").child(idRuanganSchedule!!).child("schedule")
//            .child(barang!!.key!!).child("day").get().addOnSuccessListener {
//                dayEdit = it.value as String?
//                day =  dayEdit
//            }
//        mDatabaseReference!!.child("new/ruangan").child(idRuanganSchedule!!).child("schedule")
//            .child(barang!!.key!!).child("month").get().addOnSuccessListener {
//                monthEdit = it.value as String?
//                month = monthEdit
//            }
//        mDatabaseReference!!.child("new/ruangan").child(idRuanganSchedule!!).child("schedule")
//            .child(barang!!.key!!).child("year").get().addOnSuccessListener {
//                yearEdit = it.value as String?
//                year = yearEdit
//            }
//        mDatabaseReference!!.child("new/ruangan").child(idRuanganSchedule!!).child("schedule")
//            .child(barang!!.key!!).child("hour").get().addOnSuccessListener {
//                hourEdit = it.value as String?
//                hour = hourEdit
//            }
//        mDatabaseReference!!.child("new/ruangan").child(idRuanganSchedule!!).child("schedule")
//            .child(barang!!.key!!).child("minute").get().addOnSuccessListener {
//                minuteEdit = it.value as String?
//                minute = minuteEdit
//            }
//
//        mDatabaseReference!!.child("new/ruangan").child(idRuanganSchedule!!).child("schedule")
//            .child(barang!!.key!!).child("state").get().addOnSuccessListener {
//                stateLamp2= it.value as String?
//                btnState.setText(stateLamp2)
//            }
//        mDatabaseReference!!.child("new/ruangan").child(idRuanganSchedule!!).child("schedule")
//            .child(barang!!.key!!).get().addOnSuccessListener {
////                log(it.value as String?)
//                Log.e("firebase", "data schedule : ${it.value}" )
//                val jsonArray = JSONTokener(it.value.toString()).nextValue() as JSONArray
//                for (i in 0 until jsonArray.length()) {
//                    // ID
//                    val hour = jsonArray.getJSONObject(i).getString("hour")
//                    Log.i("hour: ", hour)
//
//                    val month = jsonArray.getJSONObject(i).getString("month")
//                    Log.i("month: ", month)
//
//                    val year = jsonArray.getJSONObject(i).getString("year")
//                    Log.i("year: ", year)
//
//                    val state = jsonArray.getJSONObject(i).getString("state")
//                    Log.i("state: ", state)
//
//                    // Employee Name
//                    val time = jsonArray.getJSONObject(i).getString("time")
//                    Log.i("time: ", time)
//
//                    // Employee Salary
//                    val day = jsonArray.getJSONObject(i).getString("day")
//                    Log.i("day: ", day)
//
//                    val minute = jsonArray.getJSONObject(i).getString("minute")
//                    Log.i("minute: ", minute)
//
//                }
//            }
//
//        val schedule = findViewById<FrameLayout>(R.id.sheet)
//        schedule.visibility = View.VISIBLE
//        val imgCancelSchedule = findViewById<ImageView>(R.id.imgShceduleCancel)
//        imgCancelSchedule.setOnClickListener {
//            schedule.visibility = View.GONE
//        }
//        val tvSaveSchedule = findViewById<TextView>(R.id.tvSchedulSave)
//        val datePicker = findViewById<DatePicker>(R.id.date_Picker)
////        Toast.makeText(this, dayEdit, Toast.LENGTH_SHORT).show()
////        val today = date
////        val today = Calendar.getInstance()
//
////        Toast.makeText(applicationContext, today.toString(), Toast.LENGTH_LONG).show()
//
//        val stateLamp = btnState.text
//
//        dateLamp = day+"/"+month+"/"+year
//        dateLampId = day+month+year
////        datePicker.init(
////            year!!.toInt(),
////            month!!.toInt(),
////            day!!.toInt()
////        ) { view, year, month, day ->
////            val msg = "You Selected: $day/$month/$year"
////            dateLamp = day.toString()+"/"+month+"/"+year
//////            Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
////        }
//
//        val simpleTimePicker = findViewById<View>(R.id.timePicker) as TimePicker
//        simpleTimePicker.setIs24HourView(true) // used to display AM/PM mode
//
//        simpleTimePicker.setOnTimeChangedListener(OnTimeChangedListener { view, hour, minute     ->
////            Toast.makeText(applicationContext, "$hourOfDay  $minute", Toast.LENGTH_SHORT).show()
//            timeLamp = hour.toString() +":"+ minute
//        })
//        timeLamp = hour
//
//        tvSaveSchedule.setOnClickListener {
//            mDatabaseReference!!.child("new/ruangan").child(idRuanganSchedule!!)
//                .child("schedule").child(barang!!.key!!).child("state").setValue(stateLamp)
//            mDatabaseReference!!.child("new/ruangan").child(idRuanganSchedule!!)
//                .child("schedule").child(barang!!.key!!).child("day").setValue(day)
//            mDatabaseReference!!.child("new/ruangan").child(idRuanganSchedule!!)
//                .child("schedule").child(barang!!.key!!).child("month").setValue(month)
//            mDatabaseReference!!.child("new/ruangan").child(idRuanganSchedule!!)
//                .child("schedule").child(barang!!.key!!).child("year").setValue(year)
//            mDatabaseReference!!.child("new/ruangan").child(idRuanganSchedule!!)
//                .child("schedule").child(barang!!.key!!).child("hour").setValue(hour)
//            mDatabaseReference!!.child("new/ruangan").child(idRuanganSchedule!!)
//                .child("schedule").child(barang!!.key!!).child("minute").setValue(minute)
//            mDatabaseReference!!.child("new/ruangan").child(idRuanganSchedule!!)
//                .child("schedule").child(barang!!.key!!).child("time").setValue(timeLamp)
//        }

    }

    override fun onItemClickSceduleDelete(barang: ModelBarangSchedule?, position: Int) {
//        mDatabaseReference2 = mFirebaseInstance!!.getReference("smartButton/new/ruangan").child(barang!!.key!!)
//            .child("schedule").child(barang!!.key!!)
        Toast.makeText(this, idRuanganSchedule, Toast.LENGTH_SHORT).show()
        if (mDatabaseReference != null) {
//            deleteDataSchedule(barang!!)

            mDatabaseReference!!.child("new/ruangan").child(idRuanganSchedule!!)
                .child("schedule").child(barang!!.key!!)
                .removeValue().addOnSuccessListener {
                    Toast.makeText(
                        this@MainActivity,
                        "Data berhasil di hapus !",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }

    private fun dialogSchedule(barang: ModelBarang?) {
//        Toast.makeText(applicationContext,dateLamp.toString(), Toast.LENGTH_SHORT).show()
//        Toast.makeText(applicationContext,timeLamp.toString(), Toast.LENGTH_SHORT).show()
        val btnState = findViewById<ToggleButton>(R.id.btnScheduleState)
        val stateLamp = btnState.text
        val stateSchedule = stateLamp.toString()
        val waktuSchedule  = dateLamp+" "+timeLamp

        val day = day
        val month = month
        val year = year
        val hour = hour
        val minute = minute


        val waktuScheduleId = dateLampId+timeLamp

//        submitDataSchedule(ModelBarang(stateSchedule, waktuSchedule))
        mDatabaseReference!!.child("new/ruangan").child(barang!!.key!!).child("schedule")
            .child(waktuScheduleId)
            .setValue(ModelBarang(stateSchedule,waktuSchedule, day!!, month!!, year!!, hour!!, minute!!)).addOnSuccessListener (
                this
            ){
                val schedule = findViewById<FrameLayout>(R.id.sheet)
                schedule.visibility = View.GONE
                Toast.makeText(
                    this@MainActivity,
                    "Schedule added!",
                    Toast.LENGTH_LONG
                ).show()

            }
    }

    private fun deleteDataSchedule(barang: ModelBarangSchedule) {
//        mDatabaseReference!!.child("data_ruangan").push()
//        mDatabaseReference2!!.child(barang!!.key!!).removeValue().addOnSuccessListener {
//                    Toast.makeText(
//                        this@MainActivity,
//                        "Data berhasil di hapus !",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
    }

//    override fun onDataClick(barang: ModelBarang?, position: Int) {
//
//        val builder = AlertDialog.Builder(this)
//        builder.setTitle("Pilih Aksi")
//        builder.setPositiveButton(
//            "UPDATE"
//        ) { dialog, id -> dialogUpdateBarang(barang) }
//
//        builder.setNegativeButton(
//            "HAPUS"
//        ) { dialog, id -> hapusDataBarang(barang!!) }
//
//        builder.setNeutralButton(
//            "BATAL"
//        ) { dialog, id -> dialog.dismiss() }
//
//        val dialog: Dialog = builder.create()
//        dialog.show()
//
////        val btnLampu = findViewById<Button>(R.id.btnLampu)
////        btnLampu.setOnClickListener {
////                id -> hapusDataBarang(barang!!)
//////            val txt2 = holder.namaBarang.text.toString()
//////            Toast.makeText(this, "show :"+txt2,Toast.LENGTH_SHORT).show()
////        }
//    }

    private fun dialogTambahBarang() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Tambah Ruangan")
        val view = layoutInflater.inflate(R.layout.layout_edit_barang, null)
//        mEditNama = view.findViewById(R.id.nama_barang)
        idAlat = view.findViewById(R.id.idAlat)
        builder.setView(view)
        builder.setPositiveButton(
            "SIMPAN"
        ) { dialog, id ->
//            val namaBarang = mEditNama!!.getText().toString()
            idRuangan = idAlat!!.getText().toString()
            val namaBarang = idRuangan
            val lampuRuangan = "off"
            val lampuRuangan2 = "off"
            val idStat = "No"
//            val lampuRuangan3 = "off"
            if (!idRuangan!!.isEmpty()) {
                mDatabaseReference!!.child("new").child(idRuangan!!).get().addOnSuccessListener {
                    Log.i("firebase", "Got value ${it.value}")
                    if(it.value == null){
                        submitDataBarang(ModelBarang(namaBarang,idRuangan, lampuRuangan, lampuRuangan2, idStat))
                        mDatabaseReference!!.child("new/update/id").setValue(idRuangan)
                        mDatabaseReference!!.child("new/update/state").setValue("1")
                    }
                    else{
                        Toast.makeText(this, "ID Have Been Created Please Insert New ID!", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener{
                    Log.e("firebase", "Error getting data", it)
                }
            } else {
                Toast.makeText(this@MainActivity, "Data harus di isi!", Toast.LENGTH_LONG).show()
            }
        }
        builder.setNegativeButton(
            "BATAL"
        ) { dialog, id -> dialog.dismiss() }
        val dialog: Dialog = builder.create()
        dialog.show()
    }
    private fun dialogTambahWifi() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Update Wifi")
        val view = layoutInflater.inflate(R.layout.layout_add_wifi, null)

        namaWifi = view.findViewById(R.id.nama_wifi)
        passWifi = view.findViewById(R.id.pass_wifi)

        mDatabaseReference!!.child("wifi").child("ssid").get().addOnSuccessListener {
            val ssid = it.value
            namaWifi!!.setText(ssid.toString())
        }

        mDatabaseReference!!.child("wifi").child("pass").get().addOnSuccessListener {
            val pass = it.value
            namaWifi!!.setText(pass.toString())
        }

        builder.setView(view)
        builder.setPositiveButton(
            "SIMPAN"
        ) { dialog, id ->
            val ssid = namaWifi!!.getText().toString()
            val pass = passWifi!!.text.toString()
            if (!ssid.isEmpty()&&!pass.isEmpty()) {
                mDatabaseReference!!.child("wifi").child("ssid").setValue(ssid)
                mDatabaseReference!!.child("wifi").child("pass").setValue(pass)
                mDatabaseReference!!.child("wifi").child("stateWifi").setValue("1")
            } else {
                Toast.makeText(this@MainActivity, "Data harus di isi!", Toast.LENGTH_LONG).show()
            }
        }
        builder.setNegativeButton(
            "BATAL"
        ) { dialog, id -> dialog.dismiss() }
        val dialog: Dialog = builder.create()
        dialog.show()
    }

    private fun dialogUpdateBarang(barang: ModelBarang?) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Edit Data Ruangan")
        val view = layoutInflater.inflate(R.layout.layout_edit_barang, null)
        idAlat = view.findViewById(R.id.idAlat)
        idAlat!!.setText(barang!!.nama)

        builder.setView(view)
        builder.setPositiveButton(
            "SIMPAN"
        ) { dialog, id ->
            barang.nama = idAlat!!.getText().toString()
//            updateDataBarang(barang)
            mDatabaseReference!!.child("new").child("ruangan").child(barang!!.key!!).child("nama")
                .setValue(barang.nama).addOnSuccessListener {
                    Toast.makeText(
                        this@MainActivity,
                        "Data berhasil di update !",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
        builder.setNegativeButton(
            "BATAL"
        ) { dialog, id -> dialog.dismiss() }
        val dialog: Dialog = builder.create()
        dialog.show()
    }

    private fun submitDataBarang(barang: ModelBarang) {
        mDatabaseReference!!.child("new").child("ruangan").child(idRuangan.toString())
            .setValue(barang).addOnSuccessListener(
                this
            ) {
                Toast.makeText(
                    this@MainActivity,
                    "Data barang berhasil di simpan !",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    private fun updateDataBarang(barang: ModelBarang) {
        mDatabaseReference!!.child("data_ruangan").child(barang!!.key!!)
            .setValue(barang).addOnSuccessListener {
                Toast.makeText(
                    this@MainActivity,
                    "Data berhasil di update !",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    private fun hapusDataBarang(barang: ModelBarang) {

        if (mDatabaseReference != null) {
            mDatabaseReference!!.child("new").child("ruangan").child(barang!!.key!!)
                .removeValue().addOnSuccessListener {
                    Toast.makeText(
                        this@MainActivity,
                        "Data berhasil di hapus !",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }

    }

    private fun hapusDataBarang2(barang: ModelBarang) {

        if (mDatabaseReference != null) {
            mDatabaseReference!!.child("data_ruangan").child(barang!!.key!!)
                .removeValue().addOnSuccessListener {
                    Toast.makeText(
                        this@MainActivity,
                        "Data berhasil di hapus !",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }

    }

//    private fun hapusDataBarang(barang: ModelBarang) {
//        if (mDatabaseReference != null) {
//            barang.key?.let {
//                mDatabaseReference!!.child("data_barang").child(it)
//                    .removeValue().addOnSuccessListener {
//                        Toast.makeText(
//                            this@MainActivity,
//                            "Data berhasil di hapus !",
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }
//            }
//        }
//    }

    companion object {
        fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
            val win = activity.window
            val winParams = win.attributes
            if (on) {
                winParams.flags = winParams.flags or bits
            } else {
                winParams.flags = winParams.flags and bits.inv()
            }
            win.attributes = winParams
        }
    }
}