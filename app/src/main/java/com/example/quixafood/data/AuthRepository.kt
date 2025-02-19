package com.example.quixafood.data

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class AuthRepository {
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun registeruser(
        email: String,
        password: String,
        name: String
    ) : Boolean {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid

            if(uid != null) {
                val user = hashMapOf(
                    "uid" to uid,
                    "name" to name,
                    "email" to email,
                    "created_at" to System.currentTimeMillis()
                )
                firestore.collection("users").document(uid).set(user).await()
            }

            true
        } catch(e: Exception) {
            Log.e("error", "Erro no cadastro")
            false
        }
    }

    suspend fun loginUser(
        email: String,
        password: String,
    ): Boolean {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) {
            Log.e("error", "Erro no login")
            false
        }
    }

    suspend fun resetPassword(
        email: String
    ) :  Boolean {
        return try {
            auth.sendPasswordResetEmail(email).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getUserName(): String? {
        return try {
            val uid = auth.currentUser?.uid
            if(uid != null) {
                val snapshot = firestore.collection("users").document(uid).get().await()
                snapshot.getString("name")
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("error", "Erro ao recuperar nome")
            null
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun isuserlogged(): Boolean {
        return auth.currentUser != null
    }

}