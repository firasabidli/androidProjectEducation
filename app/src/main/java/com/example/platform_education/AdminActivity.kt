package com.example.platform_education

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class AdminActivity : AppCompatActivity() {
    lateinit var btnstart:Button
    lateinit var btnHome:Button
    lateinit var btnEtudiant:Button
    lateinit var btnEnse:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        btnstart=findViewById(R.id.buttonStart)
        btnstart.setOnClickListener{
            val intent = Intent(this, ProfileAdminActivity::class.java)
            startActivity(intent)
        }
//        btnHome=findViewById(R.id.imageButtonHome)
//        btnEtudiant=findViewById(R.id.imageButtonliste1)
//        btnEnse=findViewById(R.id.imageButtonliste2)
//
//        btnHome.setOnClickListener{
//            val intent = Intent(this, AdminActivity::class.java)
//            startActivity(intent)
//        }
//        btnEtudiant.setOnClickListener{
//            val intent = Intent(this, ProfileAdminActivity::class.java)
//            startActivity(intent)
//        }
//        btnEnse.setOnClickListener{
//            val intent = Intent(this, TeacherActivity::class.java)
//            startActivity(intent)
//        }

    }
}