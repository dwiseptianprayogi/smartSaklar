package com.example.smartsaklar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartsaklar.schedule.MainAdapterSchedule
import com.example.smartsaklar.schedule.ModelBarangSchedule
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import java.util.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ScheduleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScheduleFragment : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: MainAdapter? = null
    private var daftarBarang: ArrayList<ModelBarang?>? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var mFirebaseInstance: FirebaseDatabase? = null

    private var mRecyclerViewSchedule: RecyclerView? = null
    private var mAdapterSchedule: MainAdapterSchedule? = null
    private var daftarSchedule: ArrayList<ModelBarangSchedule?>? = null
    private var mDatabaseReferenceSchedule: DatabaseReference? = null
    private var mFirebaseInstanceSchedule: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ScheduleFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mFirebaseInstance = FirebaseDatabase.getInstance()
        mDatabaseReference = mFirebaseInstance!!.getReference("smartButton")

        val schedule = view.findViewById<FrameLayout>(R.id.sheetSchedule)
//        schedule.visibility = View.VISIBLE
        val imgCancelSchedule = view.findViewById<ImageView>(R.id.imgShceduleCancelShow)
        imgCancelSchedule.setOnClickListener {
            dismiss()
        }
        mRecyclerViewSchedule = view.findViewById(R.id.rvSchedule)
        mRecyclerViewSchedule!!.setHasFixedSize(true)
        mRecyclerViewSchedule!!.setLayoutManager(LinearLayoutManager(requireContext()))
        FirebaseApp.initializeApp(requireContext())
        mFirebaseInstanceSchedule = FirebaseDatabase.getInstance()
        mDatabaseReferenceSchedule = mFirebaseInstanceSchedule!!.getReference("smartButton")
//        mDatabaseReferenceSchedule!!.child("data_ruangan").child(barang!!.key!!).child("schedule")
//            .addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    daftarSchedule = ArrayList()
//                    for (mDataSnapshot in dataSnapshot.children) {
//                        val barang = mDataSnapshot.getValue(ModelBarangSchedule::class.java)!!
//                        barang.key = mDataSnapshot.key
//                        daftarSchedule!!.add(barang)
//                    }
//                    mAdapterSchedule = MainAdapterSchedule(context!!, daftarSchedule)
//                    mRecyclerViewSchedule!!.setAdapter(mAdapterSchedule)
//                }
//
//                override fun onCancelled(databaseError: DatabaseError) {
//                    Toast.makeText(
//                        context,
//                        databaseError.details + " " + databaseError.message, Toast.LENGTH_LONG
//                    ).show()
//                }
//            })
    }

}