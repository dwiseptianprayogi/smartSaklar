package com.example.smartsaklar

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
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
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import java.util.*


class MainActivity : AppCompatActivity(), MainAdapter.FirebaseDataListener, MainAdapterSchedule.FirebaseDataListener {
    private var mFloatingActionButton: ExtendedFloatingActionButton? = null
    private var mEditNama: EditText? = null
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: MainAdapter? = null
    private var daftarBarang: ArrayList<ModelBarang?>? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var mFirebaseInstance: FirebaseDatabase? = null

    private var timeLamp : String? = null
    private var dateLamp : String? = null
    private var hour : String? = null
    private var minute : String? = null

    private var day : String? = null
    private var month : String? = null
    private var year : String? = null

    private var mRecyclerViewSchedule: RecyclerView? = null
    private var mAdapterSchedule: MainAdapterSchedule? = null
    private var daftarSchedule: ArrayList<ModelBarangSchedule?>? = null
    private var mDatabaseReferenceSchedule: DatabaseReference? = null
    private var mFirebaseInstanceSchedule: FirebaseDatabase? = null

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
        mDatabaseReference!!.child("data_ruangan")
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
        mFloatingActionButton = findViewById(R.id.tambah_barang)
        mFloatingActionButton!!.setOnClickListener(View.OnClickListener { dialogTambahBarang() })

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
//        Toast.makeText(this, "show : "+ position,Toast.LENGTH_SHORT).show()
        val btn1 = barang!!.lampu1
        val btn2 = barang.lampu2
        if (btn1=="on"||btn2=="on"){
            barang.lampu1 = "off"
            barang.lampu2 = "off"
            val nama = barang.nama

            mDatabaseReference!!.child("state").setValue("1")
            mDatabaseReference!!.child("data_ruangan").child(barang!!.key!!).child("lampu1")
                .setValue(barang.lampu1).addOnSuccessListener {
                    Toast.makeText(
                        this@MainActivity,
                        "Data berhasil di update !",
                        Toast.LENGTH_LONG
                    ).show()
                }
            mDatabaseReference!!.child("data_ruangan").child(barang!!.key!!).child("lampu2")
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
            mDatabaseReference!!.child("state").setValue("1")
            mDatabaseReference!!.child("data_ruangan").child(barang!!.key!!).child("lampu1")
                .setValue(barang.lampu1).addOnSuccessListener {
                    Toast.makeText(
                        this@MainActivity,
                        "Data berhasil di update !",
                        Toast.LENGTH_LONG
                    ).show()
                }
            mDatabaseReference!!.child("data_ruangan").child(barang!!.key!!).child("lampu2")
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
            mDatabaseReference!!.child("state").setValue("1")
            mDatabaseReference!!.child("data_ruangan").child(barang!!.key!!).child("lampu1")
                .setValue(barang.lampu1).addOnSuccessListener {
                    Toast.makeText(
                        this@MainActivity,
                        "Data berhasil di update !",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
        if (btn1=="on"){
            barang.lampu1 = "off"
//            updateDataBarang(barang)
            mDatabaseReference!!.child("state").setValue("1")
            mDatabaseReference!!.child("data_ruangan").child(barang!!.key!!).child("lampu1")
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
            mDatabaseReference!!.child("state").setValue("1")
            mDatabaseReference!!.child("data_ruangan").child(barang!!.key!!).child("lampu2")
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
            mDatabaseReference!!.child("state").setValue("1")
            mDatabaseReference!!.child("data_ruangan").child(barang!!.key!!).child("lampu2")
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
        dateLamp = day+"/"+month+"/"+year
        datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ) { view, year, month, day ->
            val month = month + 1
            val msg = "You Selected: $day/$month/$year"
            dateLamp = day.toString()+"/"+month+"/"+year
            Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
        }
        val simpleTimePicker = findViewById<View>(R.id.timePicker) as TimePicker
        simpleTimePicker.setIs24HourView(true) // used to display AM/PM mode
        hour = simpleTimePicker.hour.toString()
        minute = simpleTimePicker.minute.toString()
        simpleTimePicker.setOnTimeChangedListener(OnTimeChangedListener { view, hourOfDay, minute ->
            Toast.makeText(applicationContext, "$hourOfDay  $minute", Toast.LENGTH_SHORT).show()
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
        mRecyclerViewSchedule = findViewById(R.id.rvSchedule)
        mRecyclerViewSchedule!!.setHasFixedSize(true)
        mRecyclerViewSchedule!!.setLayoutManager(LinearLayoutManager(this))
        FirebaseApp.initializeApp(this)
        mFirebaseInstanceSchedule = FirebaseDatabase.getInstance()
        mDatabaseReferenceSchedule = mFirebaseInstanceSchedule!!.getReference("smartButton")
        mDatabaseReferenceSchedule!!.child("data_ruangan").child(barang!!.key!!).child("schedule")
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

    override fun onItemClickSceduleEdit(barang: ModelBarangSchedule?, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onItemClickSceduleDelete(barang: ModelBarangSchedule?, position: Int) {
        if (mDatabaseReference != null) {
            mDatabaseReference!!.child("data_ruangan").child(barang!!.key!!).child("schedule").child(barang.key!!)
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
        Toast.makeText(applicationContext,dateLamp.toString(), Toast.LENGTH_SHORT).show()
        Toast.makeText(applicationContext,timeLamp.toString(), Toast.LENGTH_SHORT).show()
        val btnState = findViewById<ToggleButton>(R.id.btnScheduleState)
        val stateLamp = btnState.text
        val waktuSchedule = stateLamp.toString()
        val stateSchedule = dateLamp+" "+timeLamp

        submitDataSchedule(ModelBarang(waktuSchedule, stateSchedule))
        mDatabaseReference!!.child("data_ruangan").child(barang!!.key!!).child("schedule").push()
            .setValue(ModelBarang(waktuSchedule, stateSchedule)).addOnSuccessListener (
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

    private fun submitDataSchedule(barang: ModelBarang) {
//        mDatabaseReference!!.child("data_ruangan").push()

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
        mEditNama = view.findViewById(R.id.nama_barang)
        builder.setView(view)
        builder.setPositiveButton(
            "SIMPAN"
        ) { dialog, id ->
            val namaBarang = mEditNama!!.getText().toString()
            val lampuRuangan = "off"
            val lampuRuangan2 = "off"
//            val lampuRuangan3 = "off"
            if (!namaBarang.isEmpty()) {
                submitDataBarang(ModelBarang(namaBarang, lampuRuangan , lampuRuangan2))
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
        mEditNama = view.findViewById(R.id.nama_barang)
        mEditNama!!.setText(barang!!.nama)

        builder.setView(view)
        builder.setPositiveButton(
            "SIMPAN"
        ) { dialog, id ->
            barang.nama = mEditNama!!.getText().toString()
//            updateDataBarang(barang)
            mDatabaseReference!!.child("data_ruangan").child(barang!!.key!!)
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
        mDatabaseReference!!.child("data_ruangan").push()
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