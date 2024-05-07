package com.example.sistempencatatandanpenagihanenergilistrik

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sistempencatatandanpenagihanenergilistrik.databinding.ActivityKontrollingBinding
import com.example.sistempencatatandanpenagihanenergilistrik.databinding.ActivityPembayaranBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class PembayaranActivity : AppCompatActivity() {
    private var _binding: ActivityPembayaranBinding? = null
    private val binding get() = _binding!!
    val db = Firebase.firestore
    lateinit var buttonback : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityPembayaranBinding.inflate(layoutInflater)
        setContentView(binding.root)
        buttonback = findViewById<ImageView>(R.id.back)
        buttonback.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}