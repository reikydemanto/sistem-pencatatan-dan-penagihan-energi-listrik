package com.example.sistempencatatandanpenagihanenergilistrik

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sistempencatatandanpenagihanenergilistrik.databinding.ActivityMainBinding
import com.example.sistempencatatandanpenagihanenergilistrik.databinding.ActivityMonitoringBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MonitoringActivity : AppCompatActivity() {
    private var _binding: ActivityMonitoringBinding? = null
    private val binding get() = _binding!!
    val db = Firebase.firestore

    lateinit var buttonback : ImageView

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDayOfMonth(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date // Setel tanggal pada objek Calendar
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    data class Data(val value: Double, val roomId: Double, val date: Date)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityMonitoringBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonback = findViewById<ImageView>(R.id.back)
        buttonback.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val data = intent.getStringExtra("idKamar")
        val doubleValueKamar: Double = data!!.toDouble()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val firstDayOfMonth = calendar.time
        val lastDayOfMonths = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        calendar.set(Calendar.DAY_OF_MONTH, lastDayOfMonths)
        val lastDayOfMonth = calendar.time
        val collectionRef = db.collection("Record_Listrik")
        val mutableList = mutableListOf<Pair<String, Float>>()

        collectionRef.whereNotEqualTo("hari", "NULL").whereGreaterThan("hari", firstDayOfMonth)
            .whereLessThan("hari", lastDayOfMonth).get().addOnSuccessListener { result ->
                val data = ArrayList<String>()
                for (document: QueryDocumentSnapshot in result) {
                    val field1: Double? = document.getDouble("watt")
                    val doubleValue: Double = field1?.toDouble() ?: 0.0
                    val field1Float: Float = doubleValue.toFloat()
                    val field2 = document.getDouble("id_kamar")
                    val field3 = document.getDate("hari")
                    val label = getDayOfMonth(field3 as Date).toString()
                    val record = "$field1, $field2 ,$field3"

                    if (field2 != null) {
                        if (field2.equals(doubleValueKamar)) {
                            mutableList.add(label to field1Float)
                            data.add(record)
                        }
                    }
                }
                println(mutableList)
                binding.apply {
                    barChart.animation.duration = MonitoringActivity.animationDuration
                    barChart.animate(mutableList)
                }
                val formattedData = data.map {
                    val parts = it.split(",")
                    Data(
                        parts[0].trim().toDouble(), // nilai
                        parts[1].trim().toDouble(), // id kamar
                        SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US).parse(parts[2].trim()) // tanggal
                    )
                }

                // Filter hanya data yang terjadi pada hari Jumat
                val fridays = formattedData.filter { Calendar.getInstance().apply { time = it.date }.get(
                    Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY }

                // Menemukan Jumat terakhir
                val latestFriday = fridays.maxByOrNull { it.date }

                // Filter hanya data yang terjadi pada hari Jumat
                val saturday = formattedData.filter { Calendar.getInstance().apply { time = it.date }.get(
                    Calendar.DAY_OF_WEEK) == Calendar.TUESDAY }

                // Menemukan Jumat terakhir
                val latestSaturday = saturday.maxByOrNull { it.date }

                // Filter hanya data yang terjadi pada hari Jumat
                val sunday = formattedData.filter { Calendar.getInstance().apply { time = it.date }.get(
                    Calendar.DAY_OF_WEEK) == Calendar.MONDAY }

                // Menemukan Jumat terakhir
                val latestSunday = sunday.maxByOrNull { it.date }

                val textView: TextView = findViewById(R.id.fri)
                val textView1: TextView = findViewById(R.id.sat)
                val textView2: TextView = findViewById(R.id.sun)
                if (latestFriday != null) {
                    textView.text = latestFriday.value.toString()+" Watt"
                }
                if (latestSaturday != null) {
                    textView1.text = latestSaturday.value.toString()+" Watt"
                }
                if (latestSunday != null) {
                    textView2.text = latestSunday.value.toString()+" Watt"
                }
            }.addOnFailureListener { exception ->
                println("Gagal mengambil data: $exception")
            }
    }

    companion object {
        private const val animationDuration = 1000L
    }
}