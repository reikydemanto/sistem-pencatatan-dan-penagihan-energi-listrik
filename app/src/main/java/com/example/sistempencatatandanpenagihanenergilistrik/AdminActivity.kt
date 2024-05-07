package com.example.sistempencatatandanpenagihanenergilistrik

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sistempencatatandanpenagihanenergilistrik.databinding.ActivityAdminBinding
import com.example.sistempencatatandanpenagihanenergilistrik.databinding.ActivityPembayaranBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class AdminActivity : AppCompatActivity() {
    private var _binding: ActivityAdminBinding? = null
    private val binding get() = _binding!!
    val db = Firebase.firestore
    lateinit var kamar1: Button
    lateinit var kamar2: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        kamar1 = findViewById<Button>(R.id.kamar1)
        kamar1.setOnClickListener {
            val intent = Intent(this, MonitoringActivity::class.java)
            intent.putExtra("idKamar", "1")
            startActivity(intent)
        }

        kamar2 = findViewById<Button>(R.id.kamar2)
        kamar2.setOnClickListener {
            val intent = Intent(this, MonitoringActivity::class.java)
            intent.putExtra("idKamar", "2")
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        // Jika tombol back ditekan di halaman utama, keluar dari aplikasi
        super.onBackPressed()
        finishAffinity()
    }
}