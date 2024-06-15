package com.example.threadsappclone.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threadsappclone.model.UserModel
import com.example.threadsappclone.utils.SharedPref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.Locale
import java.util.UUID

class AuthViewModel : ViewModel() {
    val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()
    val userRef = db.getReference("users")

    private val storageRef = FirebaseStorage.getInstance().reference
    private val imageRef = storageRef.child("users/${UUID.randomUUID()}.jpg")

    private val _firebaseUser = MutableLiveData<FirebaseUser?>()
    val firebaseUser: MutableLiveData<FirebaseUser?> = _firebaseUser

    private val _error = MutableLiveData<String>()
    val error: MutableLiveData<String> = _error

    init {
        _firebaseUser.value = auth.currentUser
    }

    fun login(email: String, password: String, context: Context) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show()
                    _firebaseUser.postValue(auth.currentUser)
                    getData(auth.currentUser!!.uid, context)
                } else {
//                    Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
                    _error.postValue(it.exception?.message)
                }
            }
    }

    private fun getData(uid: String, context: Context) {
        userRef.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userData = snapshot.getValue(UserModel::class.java)
                if (userData != null) {
                    SharedPref.storeData(
                        userData.name,
                        userData.email,
                        userData.bio,
                        userData.userName,
                        userData.imageContent,
                        context
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }

    fun register(
        email: String,
        password: String,
        name: String,
        bio: String,
        userName: String,
        imageUri: Uri,
        context: Context
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(context, "Successful attempt", Toast.LENGTH_SHORT).show()
                    _firebaseUser.postValue(auth.currentUser)
                    saveImage(email, password, name, bio, userName, imageUri, auth.currentUser!!.uid, context)
                } else {
                    Toast.makeText(context, it.exception!!.message, Toast.LENGTH_SHORT).show()
                    _error.postValue("Something went wrong")
                }
            }
    }

    private fun saveImage(
        email: String,
        password: String,
        name: String,
        bio: String,
        userName: String,
        imageUri: Uri,
        uid: String,
        context: Context
    ) {
        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                saveData(email, password, name, bio, userName, uri.toString(), uid, context)
            }
        }
    }

    private fun saveData(
        email: String,
        password: String,
        name: String,
        bio: String,
        userName: String,
        imageContent: String,
        uid: String,
        context: Context
    ) {
        val firestoreDb = Firebase.firestore
        val followersRef = firestoreDb.collection("followers").document(uid)
        val followingRef = firestoreDb.collection("following").document(uid)

        // Set empty lists for followers and following
        followersRef.set(mapOf("followerIds" to listOf<String>()))
            .addOnSuccessListener {
                Log.d("Firestore", "Followers node created successfully")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error creating followers node: ${e.message}")
                _error.postValue(e.message)
            }

        followingRef.set(mapOf("followingIds" to listOf<String>()))
            .addOnSuccessListener {
                Log.d("Firestore", "Following node created successfully")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error creating following node: ${e.message}")
                _error.postValue(e.message)
            }

        val userData = UserModel(email, password, name, bio, userName, imageContent, uid)
        userRef.child(uid).setValue(userData)
            .addOnSuccessListener {
                SharedPref.storeData(name, email, bio, userName, imageContent, context)
                Log.d("Firestore", "User data saved successfully")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error saving user data: ${e.message}")
                _error.postValue(e.message)
            }
    }



    fun logout() {
//        Toast.makeText(context,"Final Logout",Toast.LENGTH_SHORT).show()
        FirebaseAuth.getInstance().signOut()
        _firebaseUser.postValue(null)
    }
}
