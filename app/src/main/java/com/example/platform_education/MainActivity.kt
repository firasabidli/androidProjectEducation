package com.example.platform_education

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainActivity : AppCompatActivity() {
private lateinit var signupText:TextView
    private lateinit var name: EditText
    private lateinit var pass: EditText
    private lateinit var loginBtn: Button
    private val apiService = RetrofitInstance.apiService
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        signupText=findViewById(R.id.signupText)
        name = findViewById(R.id.username)
        pass = findViewById(R.id.password)
        loginBtn = findViewById(R.id.loginButton)
        signupText.setOnClickListener{
            startActivity(
                Intent(
                    this@MainActivity,
                    SignUpActivity::class.java
                )
            )
        }
        loginBtn.setOnClickListener {
            val username = name.text.toString()
            val password = pass.text.toString()

            lifecycleScope.launch {
                try {
                    val loginRequest = LoginRequest(username, password)
                    val response = apiService.login(loginRequest)

                    if (response.code() in 200..299 ){
                        val loginResponse = response.body()
                        if (loginResponse != null) {
                            val username = loginResponse.username
                            val user_id=loginResponse.user_id
                            saveId(user_id)
                            saveAuthToken(username)
                            val userType = loginResponse.userType

                            when (userType) {
                                "etudiant" -> {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Welcome, Etudiant!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent =
                                        Intent(this@MainActivity, EtudiantActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }

                                "enseignant" -> {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Welcome, Enseignant!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                "admin" -> {
                                   val intent=Intent(
                                        this@MainActivity,
                                        AdminActivity::class.java
                                    )
                                    intent.putExtra("username", username)
                                    startActivity(intent)
                                    finish()
                                }

                                else -> {
                                    // Display an error message for unknown user types
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Unknown User Type",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    // You might want to take additional actions for unknown user types
                                }
                            }


                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "Login failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        ///val errorBody = response.errorBody()?.string()

                            Toast.makeText(
                                this@MainActivity,
                                "Login failed. Please check your credentials.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                } catch (e: HttpException) {
                    // Handle HTTP errors
                    Toast.makeText(
                        this@MainActivity,
                        "HTTP Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    // Handle other exceptions
                    Toast.makeText(
                        this@MainActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
        private fun saveAuthToken(username: String?) {
        // Implement your secure session management logic here
        // For example, you can store the token in SharedPreferences
        val editor = sharedPreferences.edit()
        editor.putString("username", username)
        editor.apply()
    }
    private fun saveId(user_id: String?) {
        // Implement your secure session management logic here
        // For example, you can store the token in SharedPreferences
        val editor = sharedPreferences.edit()
        editor.putString("user_id", user_id)
        editor.apply()
    }
}
