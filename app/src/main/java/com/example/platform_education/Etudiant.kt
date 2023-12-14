package com.example.platform_education

class Etudiant (val NumInscrit: Int,
                val Name: String,
                val Email: String,
                val Class: String,
                val Password: String?,
                val Etat: Int)
    {
        fun copyWithNewEtat(newEtat: Int): Etudiant {
            return Etudiant(NumInscrit, Name, Email, Class, Password, newEtat)
        }
        fun getTokenByEmail(email: String): String {
            // Implémentez la logique pour obtenir le jeton FCM correspondant à l'email
            // Retournez le jeton en tant que String
            // Exemple fictif :
            return "AAAAN-4lqM0:APA91bGPnQDNI8VsBBa-nOl_MIJ1X6CsuQ7BVmx6dGRoxg_sHWxLaKHmz-rOIKhpqvvfgXFoUOxlUDxhE0jYrxriNTfBUehXLYyVDtJDrhKB6QD7YiIqo3XMYGEHSDbcuV5Xe8ShgUzH"
        }
    }