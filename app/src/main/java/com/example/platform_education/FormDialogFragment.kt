package com.example.platform_education

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class FormDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.changepassword, null)
        val oldPasswordEditText = view.findViewById<EditText>(R.id.oldpass)
        val newPasswordEditText = view.findViewById<EditText>(R.id.newpass)
        val confirmPasswordEditText = view.findViewById<EditText>(R.id.confirmpass)

        return AlertDialog.Builder(requireContext())
            .setTitle("Formulaire")
            .setView(view)
            .setPositiveButton("OK") { dialog, which ->
                val oldPassword = oldPasswordEditText.text.toString()
                val newPassword = newPasswordEditText.text.toString()
                val confirmPassword = confirmPasswordEditText.text.toString()

                if (oldPassword.isBlank() || newPassword.isBlank() || confirmPassword.isBlank()) {
                    val snack = Snackbar.make(
                        view,
                        "Veuillez remplir tous les champs ",
                        Snackbar.LENGTH_LONG
                    )
                    snack.show()
                } else {
                    onChangePassword(oldPassword, newPassword, confirmPassword)
                }
            }
            .setNegativeButton("Annuler") { dialog, which ->
                dialog.dismiss()
            }
            .create()
    }

    fun onChangePassword(oldPassword: String, newPassword: String, confirmPassword: String) {
        try {
            val apiService = RetrofitInstance.apiService
            val userId = getUserIdFromPreferences()

            if (userId != null) {
                lifecycleScope.launch {
                    if (newPassword != confirmPassword) {
                        showToast("Les nouveaux mots de passe ne correspondent pas.")
                    } else {
                        val changePasswordRequest = ChangePasswordRequest(oldPassword, newPassword)
                        val response = apiService.changePassword(userId, changePasswordRequest)
                        Toast.makeText(requireContext(), "Reponse: ${response.isSuccessful}", Toast.LENGTH_SHORT).show()
                        if (response.isSuccessful) {
                            showToast("Mot de passe changé avec succès")
                        } else {
                            showToast("Échec du changement de mot de passe. Veuillez vérifier votre ancien mot de passe.")
                        }
                    }
                }
            } else {
                showToast("L'ID utilisateur est null")
            }
        } catch (e: Exception) {
            showToast("Erreur: ${e.message}")
            // Logguer l'erreur si nécessaire
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun getUserIdFromPreferences(): String? {
        val sharedPreferences =
            requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("user_id", null)
    }
}