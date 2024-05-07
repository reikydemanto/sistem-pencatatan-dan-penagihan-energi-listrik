package com.example.sistempencatatandanpenagihanenergilistrik

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sistempencatatandanpenagihanenergilistrik.databinding.ActivityLoginBinding
import com.example.sistempencatatandanpenagihanenergilistrik.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    lateinit var buttonBack: ImageView
    lateinit var buttonMonitoring : Button
    lateinit var buttonKontrolling : Button
    lateinit var buttonPembayaran : Button

    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("Session", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        val idKamar: Int = sharedPreferences.getInt("idKamar", 0)

        buttonBack = findViewById<ImageView>(R.id.back)
        buttonBack.setOnClickListener {
            editor.clear()
            editor.apply()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        buttonMonitoring = findViewById<Button>(R.id.monitoring)
        buttonMonitoring.setOnClickListener {
            val intent = Intent(this, MonitoringActivity::class.java)
            intent.putExtra("idKamar",idKamar.toString() )
            startActivity(intent)
        }

        buttonKontrolling = findViewById<Button>(R.id.kontroling)
        buttonKontrolling.setOnClickListener {
            val intent = Intent(this, KontrollingActivity::class.java)
            startActivity(intent)
        }

        buttonPembayaran = findViewById<Button>(R.id.pembayaran)
        buttonPembayaran.setOnClickListener {
            val intent = Intent(this, PembayaranActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        // Jika tombol back ditekan di halaman utama, keluar dari aplikasi
        super.onBackPressed()
        finishAffinity()
    }
}