package com.example.sistempencatatandanpenagihanenergilistrik

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.example.sistempencatatandanpenagihanenergilistrik.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    val db = Firebase.firestore

    lateinit var buttonLogin: Button
    lateinit var editTextUsername: EditText
    lateinit var editTextPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences = getSharedPreferences("Session", Context.MODE_PRIVATE)
        val idKamar: Int = sharedPreferences.getInt("idKamar", 0)
        if (idKamar != 0) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonLogin = findViewById<Button>(R.id.buttonLogin)
        editTextUsername = findViewById<EditText>(R.id.editTextUsername)
        editTextPassword = findViewById<EditText>(R.id.editTextPassword)


        buttonLogin.setOnClickListener {
            db.collection("User_Listrik")
                .whereEqualTo("username", editTextUsername.text.toString())
                .whereEqualTo("password", editTextPassword.text.toString())
                .limit(1)
                .get()
                .addOnSuccessListener { result ->
                    if (!result.isEmpty) {
                        val document = result.documents[0]
                        val id_kamar = document.getDouble("id_user")
                        val intValue: Int = id_kamar!!.toInt()
                        if (intValue == 0) {
                            val intent = Intent(this, AdminActivity::class.java)
                            startActivity(intent)
                        } else {
                            val intent = Intent(this, MainActivity::class.java)
                            val editor: SharedPreferences.Editor = sharedPreferences.edit()
                            editor.putInt("idKamar", intValue)
                            editor.apply()
                            startActivity(intent)
                        }
                    } else {
                        // Tampilkan pesan jika tidak ada dokumen yang ditemukan
                        Toast.makeText(
                            applicationContext,
                            "Username atau Password salah",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(
                        applicationContext,
                        "Error getting documents: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    override fun onBackPressed() {
        // Jika tombol back ditekan di halaman utama, keluar dari aplikasi
        super.onBackPressed()
        finishAffinity()
    }
}