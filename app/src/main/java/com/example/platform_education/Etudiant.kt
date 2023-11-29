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
    }