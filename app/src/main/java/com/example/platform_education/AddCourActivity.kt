package com.example.platform_education

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class AddCourActivity : AppCompatActivity() {

    private val SELECT_FILE_REQUEST_CODE = 1
    private lateinit var fichierEditText: EditText
    private var selectedFilePath: ByteArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_cour)

        val nomEditText: EditText? = findViewById(R.id.editTextNom)
        val descriptionEditText: EditText? = findViewById(R.id.editTextDescription)
        fichierEditText = findViewById(R.id.editTextFichier)
        val buttonSelectFile: Button = findViewById(R.id.buttonSelectFile)

        buttonSelectFile.setOnClickListener {
            // Launch the intent to select a file
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            startActivityForResult(intent, SELECT_FILE_REQUEST_CODE)
        }

        val apiService = RetrofitInstance.apiService

        val addButton: Button = findViewById(R.id.buttonAjouterCours)
        addButton.setOnClickListener {
            // Retrieve values when the button is clicked
            val nom = nomEditText?.text.toString()
            val description = descriptionEditText?.text.toString()

            if (selectedFilePath != null) {
                val file = createTempFile("tempFile", null, cacheDir)
                file.writeBytes(selectedFilePath!!)

                val fileSizeInBytes = file.length()
                val fileSizeInKB = fileSizeInBytes / 1024

                // Check if the file size is within acceptable limits
                val acceptableFileSizeLimitKB = 10240 // 10 MB (adjust as needed)

                if (fileSizeInKB <= acceptableFileSizeLimitKB) {
                    lifecycleScope.launch {
                        try {
                            // Prepare file part for multipart request
                            val fileRequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                            val filePart =
                                MultipartBody.Part.createFormData("fichier", file.name, fileRequestBody)

                            // Prepare other parts of the request
                            val nomRequestBody =
                                nom.toRequestBody("text/plain".toMediaTypeOrNull())
                            val descriptionRequestBody =
                                description.toRequestBody("text/plain".toMediaTypeOrNull())

                            // Call the API to add the course
                            val response = apiService.addCourse(
                                nomRequestBody,
                                descriptionRequestBody,
                                filePart
                            )

                            // Handle the API response
                            if (response.isSuccessful) {
                                withContext(Dispatchers.Main) {
                                    val responseBody = response.body()
                                    Toast.makeText(
                                        this@AddCourActivity,
                                        "Cours ajouté avec succès: $responseBody",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                withContext(Dispatchers.Main) {
                                    val errorMessage =
                                        response.errorBody()?.string() ?: "Unknown error"
                                    Log.e(
                                        "AddCourActivity",
                                        "Error during course addition. Response code: ${response.code()}, message: $errorMessage"
                                    )
                                    Toast.makeText(
                                        this@AddCourActivity,
                                        "Erreur lors de l'ajout du cours: ${response.code()}, message: $errorMessage",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } catch (e: IOException) {
                            withContext(Dispatchers.Main) {
                                // Handle IOException during file reading
                                Log.e("AddCourActivity", "Error reading file to byte array", e)
                                Toast.makeText(
                                    this@AddCourActivity,
                                    "Erreur lors de la lecture du fichier: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } finally {
                            // Delete the temporary file after use
                            file.delete()
                        }
                    }
                } else {
                    Toast.makeText(
                        this@AddCourActivity,
                        "La taille du fichier dépasse la limite autorisée.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    this@AddCourActivity,
                    "Veuillez sélectionner un fichier",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SELECT_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                // Use the file URI directly
                val fichier = readFileToByteArray(contentResolver, uri)
                if (fichier != null) {
                    selectedFilePath = fichier
                    fichierEditText.setText("File selected")
                    Toast.makeText(
                        this,
                        "Chemin du fichier ",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "Erreur lors de la récupération du chemin du fichier",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("AddCourActivity", "Erreur lors de la récupération du chemin du fichier")
                }
            }
        }
    }

    private fun encodeFileToBase64(fileData: ByteArray): String {
        return android.util.Base64.encodeToString(fileData, android.util.Base64.DEFAULT)
    }

    private fun readFileToByteArray(contentResolver: ContentResolver, uri: Uri): ByteArray? {
        return try {
            // Open the InputStream safely using the content resolver
            val inputStream: FileInputStream? =
                contentResolver.openInputStream(uri) as FileInputStream?

            // Check if the inputStream is not null
            if (inputStream != null) {
                val outputStream = ByteArrayOutputStream()
                val buffer = ByteArray(1024)
                var bytesRead: Int

                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                }

                inputStream.close()
                outputStream.toByteArray()
            } else {
                Log.e("AddCourActivity", "InputStream is null for URI: $uri")
                null
            }
        } catch (e: Exception) {
            Log.e("AddCourActivity", "Error reading file to byte array", e)
            null
        }
    }
}
