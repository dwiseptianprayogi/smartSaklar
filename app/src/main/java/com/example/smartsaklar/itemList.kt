package com.example.smartsaklar

import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.smartsaklar.schedule.MainAdapterSchedule
import com.example.smartsaklar.schedule.ModelBarangSchedule
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.ArrayList

class itemList: AppCompatActivity(), MainAdapterSchedule.FirebaseDataListener {
    private var mFloatingActionButton: ExtendedFloatingActionButton? = null
    private var mEditNama: EditText? = null
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: MainAdapter? = null
    private var daftarBarang: ArrayList<ModelBarang?>? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var mFirebaseInstance: FirebaseDatabase? = null


    override fun onItemClickSceduleEdit(barangSchedule: ModelBarangSchedule?, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onItemClickSceduleDelete(barangSchedule: ModelBarangSchedule?, position: Int) {
        TODO("Not yet implemented")
    }
}